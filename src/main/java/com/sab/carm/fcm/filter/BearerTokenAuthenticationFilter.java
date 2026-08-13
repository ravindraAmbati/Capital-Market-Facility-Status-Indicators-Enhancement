package com.sab.carm.fcm.filter;

import com.sab.carm.fcm.constants.SecurityConstants;
import com.sab.carm.fcm.audit.AuditService;
import com.sab.carm.fcm.exception.SecurityException;
import com.sab.carm.fcm.security.TokenDetails;
import com.sab.carm.fcm.security.TokenService;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Authenticates API requests using bearer tokens.
 */
@Component
public class BearerTokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final AuditService auditService;

    public BearerTokenAuthenticationFilter(TokenService tokenService, AuditService auditService) {
        this.tokenService = tokenService;
        this.auditService = auditService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(SecurityConstants.BEARER_PREFIX)) {
            try {
                TokenDetails details = tokenService.validateToken(header.substring(SecurityConstants.BEARER_PREFIX.length()),
                        request.getHeader("Origin"));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        details.getUsername(), null, details.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                .collect(Collectors.toList()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (SecurityException ex) {
                auditService.record("INVALID_TOKEN", "unknown", org.slf4j.MDC.get("correlationId"));
                throw ex;
            }
        }
        chain.doFilter(request, response);
    }
}
