package com.ttubeog.domain.auth.dto.apple;

import javax.naming.AuthenticationException;
import java.util.List;

public record ApplePublicKeyResponse (
        List<ApplePublicKey> keys
) {
    public ApplePublicKey getMatchedKey(String kid, String alg) throws AuthenticationException {
        return keys.stream()
                .filter(key -> key.kid().equals(kid) && key.alg().equals(alg))
                .findAny()
                .orElseThrow(AuthenticationException::new);
    }
}