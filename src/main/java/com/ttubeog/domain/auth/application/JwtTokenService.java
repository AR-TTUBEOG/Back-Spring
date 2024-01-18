package com.ttubeog.domain.auth.application;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Random;

@Service
public class JwtTokenService implements InitializingBean {

    private final String secretKey;
    private final long accessTokenExpirationInSeconds;
    private final long refreshTokenExpirationInSeconds;
    private static Key key;


    public JwtTokenService(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.access-expired-time}") long accessTokenExpirationInSeconds,
            @Value("${jwt.refresh-expired-time}") long refreshTokenExpirationInSeconds
    ) {
        this.secretKey = secretKey;
        this.accessTokenExpirationInSeconds = accessTokenExpirationInSeconds;
        this.refreshTokenExpirationInSeconds = refreshTokenExpirationInSeconds;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        key = getKeyFromBase64EncodedKey(encodeBase64SecretKey(secretKey));
    }

    // JWT 토큰 생성
    public String createToken(String payload, long expireLength) {
        // 토큰에 포함될 정보
        Claims claims = Jwts.claims().setSubject(payload);
        // 현재 날짜와 시간
        Date now = new Date();
        // 유효기간
        Date validity = new Date(now.getTime() + expireLength);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256) // 토큰 서명
                .compact();
    }

    // 액세스 토큰 생성
    public String createAccessToken(String payload) {
        return createToken(payload, accessTokenExpirationInSeconds);
    }

    // 리프레시 토큰 생성
    public String createRefreshToken() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String newPayload = new String(array, StandardCharsets.UTF_8);

        return createToken(newPayload, refreshTokenExpirationInSeconds);
    }

    public String getPayload(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }


    private String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Key getKeyFromBase64EncodedKey(String encodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(encodedSecretKey);

        Key key = Keys.hmacShaKeyFor(keyBytes);

        return key;
    }

    // 클라이언트 쿠키에 리프레시 토큰을 저장
    public void addRefreshTokenToCookie(String refreshToken, HttpServletResponse response) {
        Long age = refreshTokenExpirationInSeconds;
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setPath("/");
        cookie.setMaxAge(age.intValue());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
