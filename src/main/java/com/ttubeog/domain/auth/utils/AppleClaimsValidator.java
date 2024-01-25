package com.ttubeog.domain.auth.utils;

import com.ttubeog.domain.auth.utils.EncryptUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppleClaimsValidator {
    private static final String NONCE_KEY = "nonce";

    private final String iss;
    private final String clientId;
    private final String nonce;


    public AppleClaimsValidator(
            @Value("${apple.iss}") String iss,   // 애플 api 요청 경로
            @Value("${apple.bundle}") String clientId,  // apple developer에 생성된 Bundle ID 값
            @Value("1234") String nonce  // csrf 공격 방지를 위한 임의의 문자열
    ) {
        this.iss = iss;
        this.clientId = clientId;
        this.nonce = EncryptUtils.encrypt(nonce);
    }

    public boolean isValid(Claims claims) {
        return claims.getIssuer().contains(iss) &&
                claims.getAudience().equals(clientId) &&
                claims.get(NONCE_KEY, String.class).equals(nonce);
    }
}
