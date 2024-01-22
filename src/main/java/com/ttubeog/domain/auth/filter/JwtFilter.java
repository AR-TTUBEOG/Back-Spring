package com.ttubeog.domain.auth.filter;

import com.ttubeog.domain.auth.exception.CustomException;
import com.ttubeog.domain.auth.exception.ErrorCode;
import com.ttubeog.domain.auth.service.JwtTokenService;
import com.ttubeog.domain.member.application.MemberService;
import com.ttubeog.domain.member.dto.MemberDto;
import com.ttubeog.global.config.security.token.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenService jwtTokenService;
    private final MemberService memberService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        logger.info("[JwtFilter] : " + httpServletRequest.getRequestURL().toString());

        // Swagger UI 및 관련 리소스 경로에 대한 요청인 경우 토큰 검증 생략
        if (isSwaggerPath(httpServletRequest.getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        try {
            String jwt = resolveToken(httpServletRequest);
            if (StringUtils.hasText(jwt) && jwtTokenService.validateToken(jwt)) {
                authenticateWithJwtToken(jwt);
            }
        } catch (CustomException ex) {
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isSwaggerPath(String uri) {
        return uri.contains("/swagger") ||
                uri.contains("/v3/api-docs") ||
                uri.contains("/swagger-resources") ||
                uri.contains("/swagger-ui.html") ||
                uri.contains("/webjars/");
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void authenticateWithJwtToken(String jwt) {
        Long userId = Long.valueOf(jwtTokenService.getPayload(jwt));
        MemberDto user = memberService.findById(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.NOT_EXIST_USER);
        }
        UserDetails userDetails = UserPrincipal.create(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
