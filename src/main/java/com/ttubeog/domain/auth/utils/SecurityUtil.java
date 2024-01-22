package com.ttubeog.domain.auth.utils;

import com.ttubeog.domain.auth.exception.CustomException;
import com.ttubeog.domain.auth.exception.ErrorCode;
import com.ttubeog.global.config.security.token.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    private SecurityUtil() {}

    public static long getCurrentMemeberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        long memberId;

        if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            memberId = userPrincipal.getId();
        } else {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        return memberId;
    }
}
