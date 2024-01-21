package com.ttubeog.domain.auth.exception;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (CustomException ex) {
            setErrorResponse(ex.getErrorCode().getHttpStatus(), response, ex);
        } catch (Exception ex) {
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, ex);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex) throws IOException {
        logger.error("[ExceptionHandlerFilter] errMsg : " + ex.getMessage());

        response.setStatus(status.value());
        response.setContentType("application/json; charset=UTF-8");

        response.getWriter().write(
                new ErrorResponse(ex.getMessage())
                        .convertToJson()
        );
    }
}
