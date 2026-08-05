package com.company.application.filter;

import com.company.application.constants.SecurityConstants;
import com.company.application.exception.InvalidTokenException;
import com.company.application.security.TokenDetails;
import com.company.application.security.TokenService;
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

    public BearerTokenAuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
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
            } catch (InvalidTokenException ex) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
