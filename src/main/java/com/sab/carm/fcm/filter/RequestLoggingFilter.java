package com.sab.carm.fcm.filter;

import java.io.IOException;
import java.time.Instant;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Logs support-friendly request summaries without sensitive values.
 */
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        long started = System.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } finally {
            long elapsed = System.currentTimeMillis() - started;
            LOGGER.info("correlationId={} timestamp={} username={} method={} url={} executionTimeMs={} status={}",
                    MDC.get(CorrelationIdFilter.MDC_KEY), Instant.now(), username(), request.getMethod(),
                    request.getRequestURI(), elapsed, response.getStatus());
        }
    }

    private String username() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "anonymous";
        }
        return authentication.getName();
    }
}
