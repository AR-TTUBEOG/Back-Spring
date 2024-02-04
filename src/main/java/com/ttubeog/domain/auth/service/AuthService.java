package com.ttubeog.domain.auth.service;

import com.ttubeog.domain.auth.apple.AppleOAuthMemberProvider;
import com.ttubeog.domain.auth.domain.Platform;
import com.ttubeog.domain.auth.domain.Status;
import com.ttubeog.domain.auth.domain.Token;
import com.ttubeog.domain.auth.dto.KakaoInfoDto;
import com.ttubeog.domain.auth.dto.request.AppleLoginRequest;
import com.ttubeog.domain.auth.dto.response.KakaoTokenResponse;
import com.ttubeog.domain.auth.dto.response.OAuthTokenResponse;
import com.ttubeog.domain.auth.exception.NotFoundMemberException;
import com.ttubeog.domain.auth.security.JwtTokenProvider;
import com.ttubeog.domain.auth.security.OAuthPlatformMemberResponse;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AppleOAuthMemberProvider appleOAuthMemberProvider;
    private final RedisTemplate<String, String> redisTemplate;


    public OAuthTokenResponse appleOAuthLogin(AppleLoginRequest request) {
        OAuthPlatformMemberResponse applePlatformMember =
                appleOAuthMemberProvider.getApplePlatformMember(request.getToken());
        return generateOAuthTokenResponse(
                Platform.APPLE,
                applePlatformMember.getEmail(),
                applePlatformMember.getPlatformId()
        );
    }

    public KakaoTokenResponse kakaoOAuthLogin(String accessToken) {

        Member member;

        KakaoInfoDto memberInfo = getKakaoUserInfo(accessToken);

        Optional<Member> memberData = memberRepository.findByMemberNumber(String.valueOf(memberInfo.getId()));

        if (memberData.isEmpty()) {
            member = Member.builder()
                    .memberNumber(String.valueOf(memberInfo.getId()))
                    .platform(Platform.KAKAO)
                    .status(Status.ACTIVE)
                    .build();

            memberRepository.save(member);
        }

        Optional<Member> memberLoginData = memberRepository.findByMemberNumber(String.valueOf(memberInfo.getId()));

        String refreshToken = jwtTokenProvider.createRereshToken(memberLoginData.get().getId());
        redisTemplate.opsForValue().set(String.valueOf(memberLoginData.get().getId()), refreshToken);

        String memberName = memberLoginData.get().getNickname();
        if (memberName == null || memberName.isEmpty()) {
            return KakaoTokenResponse.builder()
                    .accessToken(jwtTokenProvider.createAccessToken(
                            memberLoginData.get().getId()))
                    .refreshToken(refreshToken)
                    .isRegistered(false)
                    .build();
        } else {
            return KakaoTokenResponse.builder()
                    .accessToken(jwtTokenProvider.createAccessToken(
                            memberLoginData.get().getId()))
                    .refreshToken(refreshToken)
                    .isRegistered(true)
                    .build();
        }
    }

    private OAuthTokenResponse generateOAuthTokenResponse(Platform platform, String email, String platformId) {
        return memberRepository.findIdByPlatformAndPlatformId(platform, platformId)
                .map(memberId -> {
                    Member findMember = memberRepository.findById(memberId)
                            .orElseThrow(NotFoundMemberException::new);
                    validateStatus(findMember);
                    String accessToken = issueAccessToken(findMember);
                    String refreshToken = issueRefreshToken();

                    refreshTokenService.saveTokenInfo(findMember.getId(), refreshToken, accessToken);

                    if (!findMember.isRegisteredOAuthMember()) {
                        return new OAuthTokenResponse(accessToken, refreshToken, false);
                    }
                    return new OAuthTokenResponse(accessToken, refreshToken, true);
                })
                .orElseGet(() -> {
                    Member oauthMember = new Member(email, platform, Status.ACTIVE, "");
                    Member savedMember = memberRepository.save(oauthMember);
                    String accessToken = issueAccessToken(savedMember);
                    String refreshToken = issueRefreshToken();

                    refreshTokenService.saveTokenInfo(savedMember.getId(), refreshToken, accessToken);

                    return new OAuthTokenResponse(accessToken, refreshToken, false);
                });
    }

    private String issueAccessToken(final Member findMember) {
        return jwtTokenProvider.createAccessToken(findMember.getId());
    }

    private String issueRefreshToken() {
        return Token.createRefreshToken();
    }

    private void validateStatus(final Member findMember) {
        if (findMember.getStatus() != Status.ACTIVE) {
            throw new InvalidMemberException();
        }
    }

    public KakaoInfoDto getKakaoUserInfo(String accessToken) {
        return WebClient.create()
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<KakaoInfoDto>() {
                })
                .block();
    }
}
