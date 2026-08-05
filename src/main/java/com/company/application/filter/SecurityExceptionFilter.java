package com.company.application.filter;

import com.company.application.dto.ApiErrorResponse;
import com.company.application.exception.SecurityException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Converts security exceptions raised in filters to JSON responses.
 */
@Component
public class SecurityExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public SecurityExceptionFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (SecurityException ex) {
            response.setStatus(ex.getCode().getStatus());
            response.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(),
                    new ApiErrorResponse(ex.getCode().getStatus(), ex.getMessage(), request.getRequestURI()));
        }
    }
}
