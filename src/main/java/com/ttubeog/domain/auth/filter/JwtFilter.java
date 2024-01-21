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

    // 액세스 토큰이 유효한지 확인하고 SecurityContext에 계정 정보를 저장
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        logger.info("[JwtFilter] : " + httpServletRequest.getRequestURL().toString());

        try {
            String jwt = resolveToken(httpServletRequest);

            if (StringUtils.hasText(jwt) && jwtTokenService.validateToken(jwt)) {
                Long userId = Long.valueOf(jwtTokenService.getPayload(jwt)); // 토큰에 있는 userId 가져오기
                MemberDto user = memberService.findById(userId); // userId로
                if (user == null) {
                    throw new CustomException(ErrorCode.NOT_EXIST_USER);
                }
                UserDetails userDetails = UserPrincipal.create(user);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
            }
        } catch (CustomException ex) {
            // 스웨거에 대한 예외 처리
            if (httpServletRequest.getRequestURI().contains("/swagger") || httpServletRequest.getRequestURI().contains("/v2/api-docs")) {
                // 여기에서 스웨거 예외 처리 로직을 추가
                // 예를 들어, HttpServletResponse에 적절한 응답을 설정하거나 다른 방식으로 처리
                // 아래는 예시 코드이므로 필요에 따라 수정이 필요합니다.
                servletResponse.getWriter().write("Swagger access exception: " + ex.getMessage());
                servletResponse.getWriter().flush();
                servletResponse.getWriter().close();
                return;
            } else {
                throw ex; // 스웨거 이외의 경우에는 예외를 다시 던집니다.
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    // 헤더에서 액세스 토큰 가져오는 코드
    private String resolveToken(HttpServletRequest servletRequest) {
        String bearerToken = servletRequest.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return "";
    }
}
