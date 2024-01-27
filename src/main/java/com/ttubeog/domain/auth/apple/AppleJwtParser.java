package com.ttubeog.domain.auth.apple;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttubeog.domain.auth.exception.AccessTokenExpiredException;
import com.ttubeog.domain.auth.exception.InvalidAccessTokenException;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;

@Component
public class AppleJwtParser {

    private static final String IDENTITY_TOKEN_VALUE_DELIMITER = "\\.";
    private static final int HEADER_INDEX = 0;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Map<String, String> parseHeaders(String identityToken) {
        try {
            String encodedHeader = identityToken.split(IDENTITY_TOKEN_VALUE_DELIMITER)[HEADER_INDEX];
            String decodedHeader = new String(Base64.getDecoder().decode(encodedHeader));
            return OBJECT_MAPPER.readValue(decodedHeader, Map.class);
        } catch (JsonProcessingException | ArrayIndexOutOfBoundsException e) {
            throw new InvalidAccessTokenException();
        }
    }

    public Claims parsePublicKeyAndGetClaims(String idToken, PublicKey publicKey) {
        try {
            return Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(idToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new AccessTokenExpiredException();
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new InvalidAccessTokenException();
        }
    }
}
