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

import javax.naming.spi.ResolveResult;
import java.time.LocalDateTime;
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

    // 닉네임 설정
    @Transactional
    public ResponseEntity<?> postMemberNickname(HttpServletRequest request, ProduceNicknameRequest produceNicknameRequest) {
        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.updateUserNickname(produceNicknameRequest.getNickname(), memberId);

        Optional<Member> checkMember = memberRepository.findById(memberId);

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
    // 회원탈퇴
    public ResponseEntity<?> deleteUser(HttpServletRequest request) {
        Long memberId = jwtTokenProvider.getMemberId(request);
        Optional<Member> checkMember = memberRepository.findById(memberId);

        if (checkMember.isEmpty()) {
            throw new InvalidMemberException();
        }

        Member member = checkMember.get();
        if (member.getStatus() == Status.INACTIVE) {
            return ResponseEntity.badRequest().body(new InvalidMemberException("이미 탈퇴한 회원입니다."));
        }

        member = Member.builder().status(Status.INACTIVE).build();
        memberRepository.save(member);

        LocalDateTime deleteTime = LocalDateTime.now().plusDays(WAITING_PERIOD_DAYS);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("성공적으로 회원탈퇴 되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
