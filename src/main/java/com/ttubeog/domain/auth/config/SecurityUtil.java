package com.ttubeog.domain.auth.config;


import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.global.config.security.token.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    private SecurityUtil() {}

    public static long getCurrentMemeberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
        }

        long memberId = 0;

        if (authentication.getPrincipal() instanceof Member member) {
            memberId = member.getId();
        } else {
        }
        return memberId;
    }
}