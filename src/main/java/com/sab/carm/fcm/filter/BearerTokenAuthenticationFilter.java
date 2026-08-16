package com.sab.carm.fcm.filter;

import com.sab.carm.fcm.audit.AuditEvent;
import com.sab.carm.fcm.audit.AuditService;
import com.sab.carm.fcm.constants.SecurityConstants;
import com.sab.carm.fcm.exception.SecurityException;
import com.sab.carm.fcm.security.TokenDetails;
import com.sab.carm.fcm.security.TokenService;
import com.sab.carm.fcm.util.SecurityUtil;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Authenticates API requests using bearer tokens.
 *
 * Responsibilities:
 * 1. Detect a Bearer token.
 * 2. Validate the token.
 * 3. Build the Spring Security authentication.
 * 4. Record invalid-token audit events.
 *
 * This filter does not perform LDAP authentication.
 */
@Component
public class BearerTokenAuthenticationFilter
        extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final AuditService auditService;

    public BearerTokenAuthenticationFilter(
            TokenService tokenService,
            AuditService auditService) {

        this.tokenService = tokenService;
        this.auditService = auditService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");

        if (!hasBearerToken(authorizationHeader)) {
            chain.doFilter(request, response);
            return;
        }

        String token =
                extractToken(authorizationHeader);

        try {

            TokenDetails tokenDetails =
                    tokenService.validateToken(
                            token,
                            request.getHeader("Origin"));

            setSecurityContext(tokenDetails);

        } catch (SecurityException ex) {

            recordInvalidToken(
                    request,
                    token);

            throw ex;
        }

        chain.doFilter(request, response);
    }

    private boolean hasBearerToken(
            String authorizationHeader) {

        return authorizationHeader != null
                && authorizationHeader.startsWith(
                SecurityConstants.BEARER_PREFIX);
    }

    private String extractToken(
            String authorizationHeader) {

        return authorizationHeader.substring(
                        SecurityConstants.BEARER_PREFIX.length())
                .trim();
    }

    private void setSecurityContext(
            TokenDetails tokenDetails) {

        List<SimpleGrantedAuthority> authorities =
                tokenDetails.getRoles()
                        .stream()
                        .map(role ->
                                new SimpleGrantedAuthority(
                                        "ROLE_" + role))
                        .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        tokenDetails.getUsername(),
                        null,
                        authorities);

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
    }

    private void recordInvalidToken(
            HttpServletRequest request,
            String token) {

        /*
         * Never log the actual bearer token.
         */
        auditService.record(
                AuditEvent.invalidToken(
                        "INVALID_TOKEN",
                        "unknown",
                        SecurityUtil.currentClientIp(request),
                        MDC.get("correlationId"),
                        "INVALID_OR_EXPIRED_TOKEN"));
    }

}