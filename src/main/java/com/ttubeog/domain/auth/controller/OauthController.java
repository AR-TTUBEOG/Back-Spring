package com.ttubeog.domain.auth.controller;

import com.ttubeog.domain.auth.application.OauthService;
import com.ttubeog.domain.auth.dto.OauthRequestDto;
import com.ttubeog.domain.auth.dto.OauthResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OauthController {
    private final OauthService oauthService;

    // 카카오 로그인
    @PostMapping("/auth/login/kakao")
    public OauthResponseDto loginWithKaKao(
            @RequestBody
            OauthRequestDto oauthRequestDto,
            HttpServletResponse response
    ) {
        OauthResponseDto oauthResponseDto = new OauthResponseDto();
        String accessToken = oauthService.loginWithKakao(oauthResponseDto.getAccessToken(), response);
        oauthResponseDto.setAccessToken(accessToken);
        return oauthResponseDto;
    }
}
