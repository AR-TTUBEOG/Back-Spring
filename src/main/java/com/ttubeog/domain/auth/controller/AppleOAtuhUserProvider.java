package com.ttubeog.domain.auth.controller;

import com.ttubeog.domain.auth.dto.OAuthPlatformMemberResponse;
import com.ttubeog.domain.auth.dto.apple.ApplePublicKeyResponse;
import com.ttubeog.domain.auth.exception.CustomException;
import com.ttubeog.domain.auth.exception.ErrorCode;
import com.ttubeog.domain.auth.feign.AppleAuthClient;
import com.ttubeog.domain.auth.service.AppleClaimsValidator;
import com.ttubeog.domain.auth.service.PublicKeyGenerator;
import com.ttubeog.domain.auth.utils.AppleJwtParser;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AppleOAtuhUserProvider {
    private final AppleJwtParser appleJwtParser;
    private final AppleAuthClient appleAuthClient;
    private final PublicKeyGenerator publicKeyGenerator;
    private final AppleClaimsValidator appleClaimsValidator;


    public OAuthPlatformMemberResponse getApplePlatformMember(String identityToken) {
        Map<String, String > headers = appleJwtParser.parseHeaders(identityToken);
        ApplePublicKeyResponse applePublicKeyResponse = appleAuthClient.getAppleAuthPublicKey();

        PublicKey publicKey = publicKeyGenerator.generatePublicKey(headers, applePublicKeyResponse);

        Claims claims = appleJwtParser.parsePublicKeyAndGetClaims(identityToken, publicKey);
        validateClaims(claims);
        return new OAuthPlatformMemberResponse(claims.getSubject(), claims.get("email", String.class));
    }

    private void validateClaims(Claims claims) {
        if (!appleClaimsValidator.isValid(claims)) {
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN_CLAIMS);
        }
    }
}
