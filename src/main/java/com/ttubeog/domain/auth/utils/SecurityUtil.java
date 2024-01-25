package com.ttubeog.domain.auth.utils;

import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.global.error.DefaultException;
import com.ttubeog.global.payload.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    private SecurityUtil() {}

    public static long getCurrentMemeberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new DefaultException(ErrorCode.INVALID_CHECK);
        }

        long memberId;

        if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            memberId = userPrincipal.getId();
        } else {
            throw new DefaultException(ErrorCode.INVALID_PARAMETER);
        }
        return memberId;
    }
}
