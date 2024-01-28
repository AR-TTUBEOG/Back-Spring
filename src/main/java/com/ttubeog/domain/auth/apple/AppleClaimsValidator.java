package com.ttubeog.domain.auth.apple;

import com.ttubeog.domain.auth.config.EncryptUtils;
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
            @Value("${apple.iss}") String iss,
            @Value("${apple.bundle}") String clientId,
            @Value("${apple.aud}") String nonce
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
