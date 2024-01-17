package com.ttubeog.domain.auth.application;

import com.ttubeog.domain.member.application.MemberService;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.dto.MemberDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OauthService {
    private final MemberService memberService;
    private final JwtTokenService jwtTokenService;
    private final KakaoOauthService kakaoOauthService;

    // 카카오 로그인
    public String loginWithKakao(String accessToken, HttpServletResponse response) {
        MemberDto memberDto = kakaoOauthService.getMemberProfileByToken(accessToken);
        return getTokens(memberDto.getId(), response);
    }

    // 액세스, 리프레시 토큰 생성
    public String getTokens(Long id, HttpServletResponse response) {
        final String accessToken = jwtTokenService.createAccessToken(id.toString());
        final String refreshToken = jwtTokenService.createRefreshToken();

        MemberDto memberDto = memberService.findById(id);
        memberDto.setRefreshToken(refreshToken);
        memberService.updateRefreshToken(memberDto);

        return accessToken;
    }
}
