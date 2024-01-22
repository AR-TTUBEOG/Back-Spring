package com.ttubeog.domain.auth.dto.apple;

public record ApplePublicKey (
        String kty,
        String kid,
        String alg,
        String n,
        String e
) {

}
