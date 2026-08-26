package com.sab.carm.fcm.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    public static final String CORRELATION_ID_HEADER =
            "X-CARM-FCM-CorrelationId";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        if (!request.getRequestURI().startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String correlationId =
                request.getHeader(CORRELATION_ID_HEADER);

        if (correlationId == null
                || correlationId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"header\":{\"status\":\"FAILED\"},"
                            + "\"body\":{\"code\":\"MISSING_CORRELATION_ID\","
                            + "\"message\":\""
                            + CORRELATION_ID_HEADER
                            + " header is mandatory\"}}");
            return;
        }

        CarmFcmTransactionContext.initialize(
                correlationId,
                UUID.randomUUID().toString());

        try {
            filterChain.doFilter(request, response);
        } finally {
            CarmFcmTransactionContext.clear();
        }
    }
}
