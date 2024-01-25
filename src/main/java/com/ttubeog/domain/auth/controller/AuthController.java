package com.ttubeog.domain.auth.controller;

import com.ttubeog.domain.auth.dto.request.KakaoLoginRequest;
import com.ttubeog.domain.auth.dto.response.OAuthTokenResponse;
import com.ttubeog.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "카카오 OAuth 로그인")
    @PostMapping("/login/kakao")
    public ResponseEntity<OAuthTokenResponse> loginKakao(@RequestBody @Valid KakaoLoginRequest request) {
        OAuthTokenResponse response = authService.kakaoOAuthLogin(request);
        return ResponseEntity.ok(response);
    }
}
