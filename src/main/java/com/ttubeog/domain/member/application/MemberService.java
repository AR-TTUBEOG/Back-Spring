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
import com.ttubeog.domain.member.dto.response.MemberDetailRes;
import com.ttubeog.domain.member.dto.response.MemberNicknameRes;
import com.ttubeog.domain.member.exception.AlreadyChangeNicknameException;
import com.ttubeog.domain.member.exception.FailureMemberDeleteException;
import com.ttubeog.domain.member.exception.InvalidAccessTokenExpiredException;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.global.DefaultAssert;
import com.ttubeog.global.payload.ApiResponse;
import com.ttubeog.global.payload.Message;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private static final int WAITING_PERIOD_DAYS = 3;



    // 현재 유저 조회
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        Long memberId = jwtTokenProvider.getMemberId(request);

        Optional<Member> checkMember = memberRepository.findById(memberId);
        DefaultAssert.isOptionalPresent(checkMember);
        Member member = checkMember.get();

        MemberDetailRes memberDetailRes = MemberDetailRes.builder()
                .id(member.getId())
                .name(member.getNickname())
                .platform(member.getPlatform())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(memberDetailRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> postMemberNickname(HttpServletRequest request, ProduceNicknameRequest produceNicknameRequest) {
        Long memberId = jwtTokenProvider.getMemberId(request);

        // 닉네임 1회 변경 여부 확인
        Optional<Member> checkMemberIsChanged = memberRepository.findById(memberId);
        if (checkMemberIsChanged.isPresent()) {
            Member member = checkMemberIsChanged.get();
            if (member.isNickNameChanged()) {
                Member checkMember = memberRepository.findById(memberId).get();

                MemberNicknameRes memberNicknameRes = MemberNicknameRes.builder()
                        .id(checkMember.getId())
                        .nickname(checkMember.getNickname())
                        .nicknameChanged(checkMember.getNicknameChange())
                        .build();

                ApiResponse apiResponse = ApiResponse.builder()
                        .check(false)
                        .information(memberNicknameRes)
                        .build();

                return ResponseEntity.ok(apiResponse);
            }
        }

        // 닉네임 업데이트
        memberRepository.updateMemberNickname(produceNicknameRequest.getNickname(), memberId);


        Optional<Member> checkMember = memberRepository.findById(memberId);
        Member member = checkMember.get();

        MemberNicknameRes memberNicknameRes = MemberNicknameRes.builder()
                .id(member.getId())
                .nickname(produceNicknameRequest.getNickname())
                .nicknameChanged(member.getNicknameChange())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(memberNicknameRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    // 닉네임 중복 확인
    public ResponseEntity<?> postMemberNicknameCheck(HttpServletRequest request, ProduceNicknameRequest produceNicknameRequest) {
        Long memberId = jwtTokenProvider.getMemberId(request);

        Boolean isNicknameUsed = memberRepository.existsByNickname(produceNicknameRequest.getNickname());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(!isNicknameUsed)
                .information("닉네임 중복이면 check -> false, 중복이 아니면 check -> true")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    // 토큰 재발급 설정
    public ResponseEntity<?> getMemberReissueToken(HttpServletRequest request) {
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

            ApiResponse apiResponse = ApiResponse.builder()
                    .check(true)
                    .information(oAuthTokenResponse)
                    .build();
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new InvalidAccessTokenExpiredException());
        }
    }

    @Transactional
    // 로그아웃
    public ResponseEntity<?> deleteLogout(HttpServletRequest request) {
        Long memberId = jwtTokenProvider.getMemberId(request);
        deleteValueByKey(String.valueOf(memberId));

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("성공적으로 로그아웃 되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public void deleteValueByKey(String key) {
        redisTemplate.delete(key);
    }

    @Transactional
    public ResponseEntity<?> deleteUser(HttpServletRequest request) {
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

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("성공적으로 회원탈퇴 되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }



    @Transactional
    public ResponseEntity<?> deleteInactiveMember() {
        try {
            // 비활성화 된 회원 찾기
            List<Member> memberStatus = memberRepository.findByStatus(Status.INACTIVE);

            // 비활성회 된 회원 탈퇴
            for (Member member : memberStatus) {
                memberRepository.delete(member);
            }

            return ResponseEntity.ok("비활성화된 회원 탈퇴 완료");
        } catch (Exception e) {
            throw new FailureMemberDeleteException();
        }
    }
}
