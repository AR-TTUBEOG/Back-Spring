package com.ttubeog.domain.auth.controller;

import com.ttubeog.domain.auth.dto.request.AppleLoginRequest;
import com.ttubeog.domain.auth.dto.request.KakaoLoginRequest;
import com.ttubeog.domain.auth.dto.response.KakaoTokenResponse;
import com.ttubeog.domain.auth.dto.response.OAuthTokenResponse;
import com.ttubeog.domain.auth.security.JwtTokenProvider;
import com.ttubeog.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OAuth", description = "로그인")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "카카오 OAuth 로그인")
    @PostMapping("/login/kakao")
    public KakaoTokenResponse loginKakao(@RequestBody @Valid KakaoLoginRequest kakaoLoginRequest, HttpServletRequest request) {
        KakaoTokenResponse reissueTokenResponseDto = authService.kakaoOAuthLogin(kakaoLoginRequest.getAccessToken());

        return reissueTokenResponseDto;
    }

    @Operation(summary = "애플 OAuth 로그인")
    @PostMapping("/login/apple")
    public ResponseEntity<OAuthTokenResponse> loginApple(@RequestBody @Valid AppleLoginRequest request) {
        OAuthTokenResponse response = authService.appleOAuthLogin(request);
        return ResponseEntity.ok(response);
    }
}
