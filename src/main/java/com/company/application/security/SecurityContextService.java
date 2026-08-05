package com.company.application.security;

import com.company.application.constants.SecurityConstants;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Provides reusable current security context helpers.
 */
@Service
public class SecurityContextService {

    private final TokenService tokenService;

    public SecurityContextService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public CurrentUser currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new CurrentUser("anonymous", java.util.Collections.emptyList(), "ANONYMOUS");
        }
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("ROLE_", ""))
                .collect(Collectors.toList());
        String type = roles.contains(SecurityConstants.ROLE_API) ? "TOKEN" : "SESSION";
        return new CurrentUser(authentication.getName(), roles, type);
    }

    public CurrentToken currentToken(HttpServletRequest request) {
        String token = bearerToken(request);
        if (!StringUtils.hasText(token)) {
            return null;
        }
        TokenDetails details = tokenService.validateToken(token, request.getHeader("Origin"));
        return new CurrentToken(token, details.getExpiresAt());
    }

    public CurrentSession currentSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Instant createdAt = Instant.ofEpochMilli(session.getCreationTime());
        Instant expiresAt = Instant.ofEpochMilli(session.getLastAccessedTime())
                .plusSeconds(session.getMaxInactiveInterval());
        return new CurrentSession(session.getId(), createdAt, expiresAt);
    }

    public String bearerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(SecurityConstants.BEARER_PREFIX)) {
            return header.substring(SecurityConstants.BEARER_PREFIX.length());
        }
        return null;
    }
}
