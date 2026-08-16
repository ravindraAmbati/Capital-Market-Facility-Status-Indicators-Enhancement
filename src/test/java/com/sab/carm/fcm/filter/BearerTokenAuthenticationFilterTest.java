package com.sab.carm.fcm.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.sab.carm.fcm.audit.AuditEvent;
import com.sab.carm.fcm.audit.AuditService;
import com.sab.carm.fcm.exception.SecurityException;
import com.sab.carm.fcm.security.TokenDetails;
import com.sab.carm.fcm.security.TokenService;
import java.time.Instant;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class BearerTokenAuthenticationFilterTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private AuditService auditService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private BearerTokenAuthenticationFilter filter;

    @BeforeEach
    void setUp() {

        filter =
                new BearerTokenAuthenticationFilter(
                        tokenService,
                        auditService);

        MDC.put(
                "correlationId",
                "correlation-123");

        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {

        SecurityContextHolder.clearContext();
        MDC.clear();
    }

    @Test
    void shouldContinueWhenAuthorizationHeaderIsMissing()
            throws Exception {

        when(request.getHeader("Authorization"))
                .thenReturn(null);

        filter.doFilter(
                request,
                response,
                filterChain);

        verify(filterChain)
                .doFilter(request, response);

        verifyNoInteractions(tokenService);
        verifyNoInteractions(auditService);
    }

    @Test
    void shouldContinueWhenAuthorizationHeaderIsNotBearer()
            throws Exception {

        when(request.getHeader("Authorization"))
                .thenReturn("Basic abc123");

        filter.doFilter(
                request,
                response,
                filterChain);

        verify(filterChain)
                .doFilter(request, response);

        verifyNoInteractions(tokenService);
        verifyNoInteractions(auditService);
    }

    @Test
    void shouldAuthenticateValidBearerToken()
            throws Exception {

        String token = "valid-token";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(request.getHeader("Origin"))
                .thenReturn("http://localhost:8080");

        TokenDetails details =
                new TokenDetails(
                        "sa-svc-carm-api",
                        Collections.singletonList("API"),
                        Instant.now().plusSeconds(1800),
                        "http://localhost:8080");

        when(tokenService.validateToken(
                token,
                "http://localhost:8080"))
                .thenReturn(details);

        filter.doFilter(
                request,
                response,
                filterChain);

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        assertEquals(
                "sa-svc-carm-api",
                authentication.getName());

        assertEquals(
                "ROLE_API",
                authentication
                        .getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority());

        verify(filterChain)
                .doFilter(request, response);

        verifyNoInteractions(auditService);
    }

    @Test
    void shouldRejectInvalidBearerToken()
            throws Exception {

        String token = "invalid-token";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(request.getRemoteAddr())
                .thenReturn("10.10.10.10");

        when(tokenService.validateToken(
                eq(token),
                any()))
                .thenThrow(
                        new SecurityException(
                                null,
                                "Invalid token"));

        assertThrows(
                SecurityException.class,
                () -> filter.doFilter(
                        request,
                        response,
                        filterChain));

        verify(auditService)
                .record(
                        any(AuditEvent.class));

        verifyNoInteractions(filterChain);
    }

    @Test
    void shouldUseFirstForwardedIp()
            throws Exception {

        String token = "invalid-token";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(request.getHeader("Origin"))
                .thenReturn(null);

        when(request.getHeader("X-Forwarded-For"))
                .thenReturn(
                        "10.10.10.10, 10.10.10.20");

        when(tokenService.validateToken(
                eq(token),
                eq(null)))
                .thenThrow(
                        new SecurityException(
                                null,
                                "Invalid token"));

        assertThrows(
                SecurityException.class,
                () -> filter.doFilter(
                        request,
                        response,
                        filterChain));

        verify(auditService)
                .record(any(AuditEvent.class));

        verifyNoInteractions(filterChain);
    }
}