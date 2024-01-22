package com.ttubeog.domain.auth.service;

import com.ttubeog.domain.member.application.MemberService;
import com.ttubeog.domain.member.dto.MemberDto;
import com.ttubeog.global.error.DefaultException;
import com.ttubeog.global.payload.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        jwtTokenService.addRefreshTokenToCookie(refreshToken, response);
        return accessToken;
    }

    // 리프레시 토큰을 액세스 토큰으로 갱신
    public String refreshToAccessToken(String refreshToken) {
        MemberDto memberDto = memberService.findByRefreshToken(refreshToken);

        if(memberDto == null) {
            throw new DefaultException(ErrorCode.INVALID_OPTIONAL_ISPRESENT);
        }

        if (!jwtTokenService.validateToken(refreshToken)) {
            throw new DefaultException(ErrorCode.INVALID_CHECK);
        }

        return jwtTokenService.createAccessToken(memberDto.getId().toString());
    }
}
