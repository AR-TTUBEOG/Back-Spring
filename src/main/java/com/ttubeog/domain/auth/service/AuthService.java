package com.ttubeog.domain.auth.service;

import com.ttubeog.domain.auth.apple.AppleOAuthMemberProvider;
import com.ttubeog.domain.auth.domain.Platform;
import com.ttubeog.domain.auth.domain.Status;
import com.ttubeog.domain.auth.domain.Token;
import com.ttubeog.domain.auth.dto.KakaoInfoDto;
import com.ttubeog.domain.auth.dto.request.AppleLoginRequest;
import com.ttubeog.domain.auth.dto.response.KakaoTokenResponse;
import com.ttubeog.domain.auth.dto.response.OAuthTokenResponse;
import com.ttubeog.domain.auth.security.JwtTokenProvider;
import com.ttubeog.domain.auth.security.OAuthPlatformMemberResponse;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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



    @Transactional
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
                    .nicknameChange(0)
                    .build();

            memberRepository.save(member);
        }

        Optional<Member> memberLoginData = memberRepository.findByMemberNumber(String.valueOf(memberInfo.getId()));

        String refreshToken = jwtTokenProvider.createRefreshToken(memberLoginData.get().getId());
        redisTemplate.opsForValue().set(String.valueOf(memberLoginData.get().getId()), refreshToken);

        String memberName = memberLoginData.get().getNickname();
        if (memberName == null || memberName.isEmpty()) {
            validateStatus(memberLoginData.get());

            return KakaoTokenResponse.builder()
                    .accessToken(jwtTokenProvider.createAccessToken(
                            memberLoginData.get().getId()))
                    .refreshToken(refreshToken)
                    .isRegistered(false)
                    .build();
        } else {
            validateStatus(memberLoginData.get());

            return KakaoTokenResponse.builder()
                    .accessToken(jwtTokenProvider.createAccessToken(
                            memberLoginData.get().getId()))
                    .refreshToken(refreshToken)
                    .isRegistered(true)
                    .build();
        }
    }

    private OAuthTokenResponse generateOAuthTokenResponse(Platform platform, String email, String platformId) {
        return memberRepository.findByMemberNumber(platformId)
                .map(existingMember -> {
                    validateStatus(existingMember);
                    String accessToken = issueAccessToken(existingMember);
                    String refreshToken = issueRefreshToken(existingMember);
                    refreshTokenService.saveTokenInfo(existingMember.getId(), refreshToken, accessToken);
                    if(existingMember.getNickname() == null) {
                        Member newMember = new Member(email, platform, Status.ACTIVE, platformId, 0);
                        Member savedMember = memberRepository.save(newMember);
                        accessToken = issueAccessToken(savedMember);
                        refreshToken = issueRefreshToken(savedMember);
                        refreshTokenService.saveTokenInfo(savedMember.getId(), refreshToken, accessToken);
                        return new OAuthTokenResponse(accessToken, refreshToken, false);
                    } else {
                        return new OAuthTokenResponse(accessToken, refreshToken, true);
                    }
                })
                .orElseGet(() -> {
                    Member newMember = new Member(email, platform, Status.ACTIVE, platformId, 0);
                    Member savedMember = memberRepository.save(newMember);
                    String accessToken = issueAccessToken(savedMember);
                    String refreshToken = issueRefreshToken(savedMember);
                    refreshTokenService.saveTokenInfo(savedMember.getId(), refreshToken, accessToken);
                    return new OAuthTokenResponse(accessToken, refreshToken, false);
                });
    }


    private String issueAccessToken(final Member findMember) {
        return jwtTokenProvider.createAccessToken(findMember.getId());
    }

    private String issueRefreshToken(final Member findMember) {
        return jwtTokenProvider.createRefreshToken(findMember.getId());
    }

    private void validateStatus(final Member member) {

        // 멤버가 탈퇴해서 INACTIVE 된 경우
        if (member.getStatus() != Status.ACTIVE) {
            Member.builder()
                    .id(member.getId())
                    .oAuthId(member.getOAuthId())
                    .nickname(member.getNickname())
                    .memberNumber(member.getMemberNumber())
                    .email(member.getEmail())
                    .platformId(member.getPlatformId())
                    .platform(member.getPlatform())
                    .status(Status.ACTIVE)  // 상태를 비활성화로 설정
                    .refreshToken(member.getRefreshToken())
                    .build();
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

