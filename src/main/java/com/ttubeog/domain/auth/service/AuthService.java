package com.ttubeog.domain.auth.service;

import com.ttubeog.domain.auth.domain.Platform;
import com.ttubeog.domain.auth.domain.Status;
import com.ttubeog.domain.auth.dto.request.KakaoLoginRequest;
import com.ttubeog.domain.auth.dto.response.OAuthTokenResponse;
import com.ttubeog.domain.auth.exception.NotFoundMemberException;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import lombok.RequiredArgsConstructor;
import org.hibernate.procedure.ParameterMisuseException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    public OAuthTokenResponse kakaoOAuthLogin(KakaoLoginRequest request) {

        return generateOAuthTokenResponse(
                Platform.KAKAO,
                request.getEmail(),
                request.getPlatformId()
        );

    }

    private OAuthTokenResponse generateOAuthTokenResponse(Platform platform, String email, String platformId) {
        return memberRepository.findIdByPlatformAndPlatformId(platform, platformId)
                .map(memberId -> {
                    Member findMember = memberRepository.findById(memberId)
                            .orElseThrow(NotFoundMemberException::new);
                    validateStatus(findMember);
                    String accessToken = issueAccessToken(findMember);
                    return new OAuthTokenResponse(accessToken);
                })
                .orElseGet(() -> {
                    Member oauthMember = new Member(email, platform, Status.ACTIVE);
                    Member savedMember = memberRepository.save(oauthMember);
                    String accessToken = issueAccessToken(savedMember);

                    return new OAuthTokenResponse(accessToken);
                });
    }

    private String issueAccessToken(final Member findMember) {
        return jwtTokenProvider.createAccessToken(findMember.getId());
    }

    private void validateStatus(final Member findMember) {
        if (findMember.getStatus() != Status.ACTIVE) {
            throw new InvalidMemberException();
        }
    }
}
