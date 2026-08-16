package com.sab.carm.fcm.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Security-related request utilities.
 */
public final class SecurityUtil {

    private static final String ANONYMOUS = "anonymous";

    private SecurityUtil() {
    }

    /**
     * Returns the currently authenticated username.
     */
    public static String currentUsername() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || authentication instanceof AnonymousAuthenticationToken
                || !authentication.isAuthenticated()) {

            return ANONYMOUS;
        }

        String username =
                authentication.getName();

        if (username == null
                || username.trim().isEmpty()) {

            return ANONYMOUS;
        }

        return username;
    }

    /**
     * Returns the client IP address.
     *
     * X-Forwarded-For is supported because the application
     * can run behind a load balancer/proxy.
     */
    public static String currentClientIp(
            HttpServletRequest request) {

        if (request == null) {
            return null;
        }

        String forwardedFor =
                request.getHeader("X-Forwarded-For");

        if (forwardedFor != null
                && !forwardedFor.trim().isEmpty()) {

            return forwardedFor
                    .split(",")[0]
                    .trim();
        }

        return request.getRemoteAddr();
    }
}