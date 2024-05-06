package com.ttubeog.domain.auth.security;

import com.ttubeog.domain.auth.security.AuthorizationExtractor;
import com.ttubeog.domain.auth.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public LoginInterceptor(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isPreflight(request) || isSwaggerRequest(request)) {
            return true;
        }

        String accessToken = AuthorizationExtractor.extractAccessToken(request);
        jwtTokenProvider.validateAccessToken(accessToken);
        return true;
    }

    private boolean isSwaggerRequest(HttpServletRequest request) {
        String  uri = request.getRequestURI();
        return uri.contains("swagger") || uri.contains("api-docs") || uri.contains("webjars");
    }

    private boolean isPreflight(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.OPTIONS.toString());
    }
}
