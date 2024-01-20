package com.ttubeog.domain.auth.filter;

import com.ttubeog.domain.auth.application.JwtTokenService;
import com.ttubeog.domain.member.application.MemberService;
import com.ttubeog.domain.member.dto.MemberDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenService jwtTokenService;
    private final MemberService memberService;

    // 액세스 토큰이 유효한지 확인하고 SecurityContext에 계정 정보를 저장
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        logger.info("[JwtFilter] : " + httpServletRequest.getRequestURL().toString());
        String jwt = resolveToken(httpServletRequest);

        if (StringUtils.hasText(jwt) && jwtTokenService.validateToken(jwt)) {
            Long memberId = Long.valueOf(jwtTokenService.getPayload(jwt));
        }
    }

    // 헤더에서 액세스 토큰 가져오는 코드
    private String resolveToken(HttpServletRequest servletRequest) {
        String bearerToken = servletRequest.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
