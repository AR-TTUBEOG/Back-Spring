package com.ttubeog.domain.auth.apple;

import com.ttubeog.domain.auth.config.EncryptUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppleClaimsValidator {

    private final String iss;
    private final String aud;

    public AppleClaimsValidator(
            @Value("${apple.iss}") String iss,
            @Value("${apple.aud}") String aud
            ) {
        this.iss = iss;
        this.aud = aud;
    }

    public boolean isValid(Claims claims) {
        return claims.getIssuer().contains(iss) &&
                claims.getAudience().equals(aud);
    }
}

