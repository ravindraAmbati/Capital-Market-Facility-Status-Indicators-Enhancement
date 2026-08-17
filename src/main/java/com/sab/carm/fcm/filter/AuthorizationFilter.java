package com.sab.carm.fcm.filter;

import com.sab.carm.fcm.audit.AuditEvent;
import com.sab.carm.fcm.audit.AuditService;
import com.sab.carm.fcm.exception.SecurityErrorCode;
import com.sab.carm.fcm.exception.SecurityException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sab.carm.fcm.util.SecurityUtil;
import org.slf4j.MDC;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Ensures that protected API requests have an authenticated user.
 *
 * Responsibilities:
 * 1. Determine whether the request requires authentication.
 * 2. Check the Spring Security authentication context.
 * 3. Audit authorization failures.
 * 4. Allow authenticated requests to continue.
 *
 * This filter does not perform authentication or authorization.
 */
@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    private static final String API_PREFIX = "/api/";

    private static final String AUTHENTICATE_ENDPOINT =
            "/api/security/authenticate";

    private static final String ANONYMOUS_USER =
            "anonymous";

    private final AuditService auditService;

    public AuthorizationFilter(
            AuditService auditService) {

        this.auditService = auditService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        if (!requiresAuthentication(request)) {
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (!isAuthenticated(authentication)) {

            recordAuthorizationFailure(request);

            throw new SecurityException(
                    SecurityErrorCode.UNAUTHORIZED,
                    "Authentication is required");
        }

        chain.doFilter(request, response);
    }

    private boolean isAuthenticated(
            Authentication authentication) {

        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication
                instanceof AnonymousAuthenticationToken);
    }

    private boolean requiresAuthentication(
            HttpServletRequest request) {

        String path =
                request.getRequestURI();

        return path != null
                && path.startsWith(API_PREFIX)
                && !AUTHENTICATE_ENDPOINT.equals(path);
    }

    private void recordAuthorizationFailure(
            HttpServletRequest request) {

        String username =
                resolveUsername();

        String clientIp =
                SecurityUtil.currentClientIp(request);

        auditService.record(
                AuditEvent.loginFailure(
                        username,
                        clientIp,
                        MDC.get("correlationId"),
                        "AUTHENTICATION_REQUIRED"));
    }

    private String resolveUsername() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || authentication instanceof AnonymousAuthenticationToken) {
            return ANONYMOUS_USER;
        }

        return authentication.getName();
    }
}