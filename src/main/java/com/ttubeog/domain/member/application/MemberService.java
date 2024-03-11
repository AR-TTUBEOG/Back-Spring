package com.ttubeog.domain.member.application;

import com.ttubeog.domain.auth.domain.Status;
import com.ttubeog.domain.auth.dto.response.OAuthTokenResponse;
import com.ttubeog.domain.auth.exception.AccessTokenExpiredException;
import com.ttubeog.domain.auth.exception.InvalidAccessTokenException;
import com.ttubeog.domain.auth.security.JwtTokenProvider;
import com.ttubeog.domain.auth.service.RefreshTokenService;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.dto.request.ProduceNicknameRequest;
import com.ttubeog.domain.member.dto.response.MemberDetailDto;
import com.ttubeog.domain.member.dto.response.MemberNicknameDto;
import com.ttubeog.domain.member.dto.response.MemberPlaceDto;
import com.ttubeog.domain.member.exception.FailureMemberDeleteException;
import com.ttubeog.domain.member.exception.InvalidAccessTokenExpiredException;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.spot.domain.repository.SpotRepository;
import com.ttubeog.domain.store.domain.Store;
import com.ttubeog.domain.store.domain.repository.StoreRepository;
import com.ttubeog.global.DefaultAssert;
import com.ttubeog.global.payload.CommonDto;
import com.ttubeog.global.payload.Message;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final RedisTemplate<String, String> redisTemplate;
    private final StoreRepository storeRepository;
    private final SpotRepository spotRepository;
    private static final int WAITING_PERIOD_DAYS = 3;

    // 현재 유저 조회
    public CommonDto getCurrentUser(HttpServletRequest request) {
        Long memberId = jwtTokenProvider.getMemberId(request);

        Optional<Member> checkMember = memberRepository.findById(memberId);
        DefaultAssert.isOptionalPresent(checkMember);
        Member member = checkMember.get();

        MemberDetailDto memberDetailDto = MemberDetailDto.builder()
                .id(member.getId())
                .name(member.getNickname())
                .platform(member.getPlatform())
                .build();

        return new CommonDto(true, memberDetailDto);
    }

    @Transactional
    public CommonDto postMemberNickname(HttpServletRequest request, ProduceNicknameRequest produceNicknameRequest) {
        Long memberId = jwtTokenProvider.getMemberId(request);

        // 닉네임 1회 변경 여부 확인
        Optional<Member> checkMemberIsChanged = memberRepository.findById(memberId);
        if (checkMemberIsChanged.isPresent()) {
            Member member = checkMemberIsChanged.get();
            if (member.isNickNameChanged() == 2) {
                Member checkMember = memberRepository.findById(memberId).get();

                MemberNicknameDto memberNicknameDto = MemberNicknameDto.builder()
                        .id(checkMember.getId())
                        .nickname(checkMember.getNickname())
                        .nicknameChanged(checkMember.getNicknameChange())
                        .build();

                return new CommonDto(false, memberNicknameDto);
            }
        }

        // 닉네임 업데이트
        memberRepository.updateMemberNickname(produceNicknameRequest.getNickname(), memberId);

        Optional<Member> checkMember = memberRepository.findById(memberId);
        Member member = checkMember.get();
        Integer nickNameChanged = member.isNickNameChanged();
        Integer newNicknameChanged = nickNameChanged += 1;
        memberRepository.updateMemberNicknameChange(newNicknameChanged, memberId);


        MemberNicknameDto memberNicknameDto = MemberNicknameDto.builder()
                .id(member.getId())
                .nickname(produceNicknameRequest.getNickname())
                .nicknameChanged(member.getNicknameChange())
                .build();

        return new CommonDto(true, memberNicknameDto);
    }

    @Transactional
    // 닉네임 중복 확인
    public CommonDto postMemberNicknameCheck(HttpServletRequest request, ProduceNicknameRequest produceNicknameRequest) {
        Long memberId = jwtTokenProvider.getMemberId(request);

        Boolean isNicknameUsed = memberRepository.existsByNickname(produceNicknameRequest.getNickname());

        return new CommonDto(!isNicknameUsed, "닉네임 중복이면 check -> false, 중복이 아니면 check -> true");
    }

    @Transactional
    // 토큰 재발급 설정
    public CommonDto getMemberReissueToken(HttpServletRequest request) {
        Long memberId;

        try {
            memberId = jwtTokenProvider.getMemberId(request);
        } catch (InvalidAccessTokenException | AccessTokenExpiredException e) {
            throw new InvalidAccessTokenException();
        }

        Optional<Member> checkMember = memberRepository.findById(memberId);
        if (checkMember.isEmpty()) {
            throw new InvalidMemberException();    // 존재 하지 않는 회원일 경우
        }

        Member member = checkMember.get();

        // 리프레시 토큰으로 새로운 액세스 토큰 발급
        try {
            String newAccessToken = jwtTokenProvider.createAccessToken(memberId);
            String newRefreshToken = jwtTokenProvider.createRefreshToken(memberId);
            refreshTokenService.saveTokenInfo(memberId, newRefreshToken, newAccessToken);


            OAuthTokenResponse oAuthTokenResponse = new OAuthTokenResponse(newAccessToken, newRefreshToken, member.isRegisteredOAuthMember());

            return new CommonDto(true, oAuthTokenResponse);
        } catch (Exception e) {
            throw new InvalidAccessTokenExpiredException();
        }
    }

    @Transactional
    // 로그아웃
    public CommonDto deleteLogout(HttpServletRequest request) {
        Long memberId = jwtTokenProvider.getMemberId(request);
        deleteValueByKey(String.valueOf(memberId));

        return new CommonDto(true, Message.builder().message("성공적으로 로그아웃 되었습니다.").build());
    }

    public void deleteValueByKey(String key) {
        redisTemplate.delete(key);
    }

    @Transactional
    public CommonDto deleteUser(HttpServletRequest request) {
        Long memberId = jwtTokenProvider.getMemberId(request);
        Optional<Member> checkMember = memberRepository.findById(memberId);

        if (checkMember.isEmpty()) {
            throw new InvalidMemberException();
        }

        Member member = checkMember.get();
        if (member.getStatus() != Status.INACTIVE) {
            member = Member.builder()
                    .id(member.getId())
                    .oAuthId(member.getOAuthId())
                    .nickname(member.getNickname())
                    .memberNumber(member.getMemberNumber())
                    .email(member.getEmail())
                    .platformId(member.getPlatformId())
                    .platform(member.getPlatform())
                    .status(Status.INACTIVE)  // 상태를 비활성화로 설정
                    .refreshToken(member.getRefreshToken())
                    .build();
            memberRepository.save(member);
        }

        return new CommonDto(true, Message.builder().message("성공적으로 회원탈퇴 되었습니다.").build());
    }

    @Transactional
    public CommonDto deleteInactiveMember() {
        try {
            // 비활성화 된 회원 찾기
            List<Member> memberStatus = memberRepository.findByStatus(Status.INACTIVE);

            // 비활성회 된 회원 탈퇴
            for (Member member : memberStatus) {
                memberRepository.delete(member);
            }

            return new CommonDto(true, "비활성화된 회원 탈퇴 완료");
        } catch (Exception e) {
            throw new FailureMemberDeleteException();
        }
    }

    @Transactional
    public CommonDto getMySpotList(HttpServletRequest request, Integer pageNum) {
        Long memberId = jwtTokenProvider.getMemberId(request);

        Page<Spot> spotPage = spotRepository.findAllByMemberId(memberId, PageRequest.of(pageNum, 10));

        List<MemberPlaceDto> memberPlaceDtoList = new ArrayList<>();

        for (Spot spot : spotPage) {
            MemberPlaceDto memberPlaceDto = MemberPlaceDto.builder()
                    .id(spot.getId())
                    .name(spot.getName())
                    .info(spot.getInfo())
                    .build();
            memberPlaceDtoList.add(memberPlaceDto);
        }

        return new CommonDto(true, memberPlaceDtoList);
    }

    @Transactional
    public CommonDto getMyStoreList(HttpServletRequest request, Integer pageNum) {
        Long memberId = jwtTokenProvider.getMemberId(request);

        List<Store> spotsByMemberId = memberRepository.findStoreByMemberId(memberId);

        Page<Store> storePage = storeRepository.findAllByMemberId(memberId, PageRequest.of(pageNum, 10));

        List<MemberPlaceDto> memberPlaceDtoList = new ArrayList<>();

        for (Store store : storePage) {
            MemberPlaceDto memberPlaceDto = MemberPlaceDto.builder()
                    .id(store.getId())
                    .name(store.getName())
                    .info(store.getInfo())
                    .build();
            memberPlaceDtoList.add(memberPlaceDto);
        }

        return new CommonDto(true, memberPlaceDtoList);
    }
}
