package com.company.application.filter;

import com.company.application.audit.AuditService;
import com.company.application.exception.SecurityErrorCode;
import com.company.application.exception.SecurityException;
import com.company.application.util.SecurityUtil;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Records authorization failures that reach protected application paths.
 */
@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    private final AuditService auditService;

    public AuthorizationFilter(AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (requiresAuthentication(request) && authentication == null) {
            auditService.record("AUTHORIZATION_FAILURE", SecurityUtil.currentUsername(), MDC.get("correlationId"));
            throw new SecurityException(SecurityErrorCode.UNAUTHORIZED, "Authentication is required");
        }
        chain.doFilter(request, response);
    }

    private boolean requiresAuthentication(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/") && !path.equals("/api/security/authenticate");
    }
}
