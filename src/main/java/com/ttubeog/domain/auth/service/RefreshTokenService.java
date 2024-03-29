package com.ttubeog.domain.auth.service;

import com.ttubeog.domain.auth.domain.Token;
import com.ttubeog.domain.auth.exception.InvalidRefreshTokenException;
import com.ttubeog.domain.auth.exception.NotFoundMemberException;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService {
    private final long validityRefreshTokenInMilliseconds;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public RefreshTokenService(@Value("${jwt.refresh-expired-time}")
                               long validityRefreshTokenInMilliseconds,
                               MemberRepository memberRepository,
                               RedisTemplate<String, Object> redisTemplate) {
        this.validityRefreshTokenInMilliseconds = validityRefreshTokenInMilliseconds;
        this.memberRepository = memberRepository;
        this.redisTemplate = redisTemplate;
    }

    public void saveTokenInfo(Long memberId, String refreshToken, String accessToken) {
        Token token = Token.builder()
                .id(memberId)
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .expiration(validityRefreshTokenInMilliseconds)
                .build();

        redisTemplate.opsForValue().set(refreshToken, token, validityRefreshTokenInMilliseconds, TimeUnit.MILLISECONDS);
    }

    public Member getMemberFromRefreshToken(String refreshToken) {
        Token token = findTokenByRefreshToken(refreshToken);
        if (token.getExpiration() > 0) {
            Long memberId = token.getId();
            return memberRepository.findById(memberId)
                    .orElseThrow(NotFoundMemberException::new);
        }
        throw new InvalidRefreshTokenException();
    }

    public Token findTokenByRefreshToken(String refreshToken) {
        Token token = (Token) redisTemplate.opsForValue().get(refreshToken);
        if (token != null) {
            return token;
        }
        throw new InvalidRefreshTokenException();
    }

    public void updateToken(Token token) {
        redisTemplate.opsForValue().set(token.getRefreshToken(), token, token.getExpiration(), TimeUnit.MILLISECONDS);
    }
}
