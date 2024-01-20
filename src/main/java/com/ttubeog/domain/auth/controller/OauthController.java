package com.ttubeog.domain.auth.controller;

import com.ttubeog.domain.auth.dto.OauthRequestDto;
import com.ttubeog.domain.auth.dto.OauthResponseDto;
import com.ttubeog.domain.auth.dto.RefreshTokenResponseDto;
import com.ttubeog.domain.auth.service.OauthService;
import com.ttubeog.global.error.DefaultException;
import com.ttubeog.global.payload.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class OauthController {
    private final OauthService oauthService;

    // 카카오 로그인
    @PostMapping("/login/kakao")
    public OauthResponseDto loginWithKaKao(
            @RequestBody
            OauthRequestDto oauthRequestDto,
            HttpServletResponse response
    ) {
        OauthResponseDto oauthResponseDto = new OauthResponseDto();
        String accessToken = oauthService.loginWithKakao(oauthRequestDto.getAccessToken(), response);
        oauthResponseDto.setAccessToken(accessToken);
        return oauthResponseDto;
    }

    // 리프레시 토큰으로 액세스토큰 재발급
    @PostMapping("/token/refresh")
    public RefreshTokenResponseDto tokenRefresh(HttpServletRequest request) {
        RefreshTokenResponseDto refreshTokenResponseDto = new RefreshTokenResponseDto();
        Cookie[] list = request.getCookies();

        if (list == null) {
            throw new DefaultException(ErrorCode.INVALID_CHECK);
        }

        Cookie refreshTokenCookie = Arrays.stream(list).filter(cookie ->
                cookie.getName().equals("refresh_token")).collect(Collectors.toList()).get(0);

        if (refreshTokenCookie == null) {
            throw new DefaultException(ErrorCode.INVALID_CHECK);
        }

        String accessToken = oauthService.refreshToAccessToken(refreshTokenCookie.getValue());
        refreshTokenResponseDto.setAccessToken(accessToken);
        return refreshTokenResponseDto;
    }
}