package com.ttubeog.domain.auth.security;

import com.ttubeog.domain.auth.exception.AccessTokenExpiredException;
import com.ttubeog.domain.auth.exception.InvalidAccessTokenException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    private final String secretKey;
    private final long validityAccessTokenInMilliseconds;
    private final long validityRefreshTokenInMilliseconds;
    private final JwtParser jwtParser;


    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey,
                            @Value("${jwt.access-expired-time}")
                            long validityAccessTokenInMilliseconds,
                            @Value("${jwt.refresh-expired-time}")
                            long validityRefreshTokenInMilliseconds) {
        this.secretKey = secretKey;
        this.validityAccessTokenInMilliseconds = validityAccessTokenInMilliseconds;
        this.validityRefreshTokenInMilliseconds = validityRefreshTokenInMilliseconds;
        this.jwtParser = Jwts.parser().setSigningKey(secretKey);
    }

    public String createAccessToken(Long memberId) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(memberId));

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityAccessTokenInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    public String createRefreshToken(Long memberId) {            // 토큰 생성
        Claims claims = Jwts.claims().setSubject(String.valueOf(memberId));
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validityRefreshTokenInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret 값 세팅
                .compact();

        return token;
    }



    public void validateAccessToken(String token) {
        try {
            jwtParser.parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new AccessTokenExpiredException();
        } catch (JwtException e) {
            throw new InvalidAccessTokenException();
        }
    }

    private boolean isExpiredAccessToken (String token) {
        try {
            jwtParser.parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            return true;
        }
        return false;
    }

    public String resolveToken(HttpServletRequest request) {
        String tokenHeader = request.getHeader("Authorization");

        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            return tokenHeader.substring(7);
        } else {
            throw new IllegalArgumentException("Invalid access token header");
        }
    }

    public String getPayload(String token) {
        try {
            return jwtParser.parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            throw new AccessTokenExpiredException();
        } catch (JwtException e) {
            throw new InvalidAccessTokenException();
        }
    }

    public Long getMemberId(HttpServletRequest request) {
        String token = resolveToken(request);
        String memberId = getPayload(token);

        return Long.valueOf(memberId);
    }
}
