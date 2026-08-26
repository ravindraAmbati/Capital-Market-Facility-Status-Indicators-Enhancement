package com.sab.carm.fcm.config;

import com.sab.carm.fcm.service.ApiAuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiAuditInterceptorTest {

    @Mock
    private ApiAuditService auditService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private ApiAuditInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new ApiAuditInterceptor(auditService);
    }

    @Test
    void shouldAuditApiRequest() {
        when(request.getRequestURI())
                .thenReturn("/api/carm/fcm/facility");
        when(request.getHeader(
                ApiAuditInterceptor.CORRELATION_ID_HEADER))
                .thenReturn("CARM-CORR-001");
        when(request.getMethod()).thenReturn("POST");
        when(response.getStatus()).thenReturn(200);

        interceptor.afterCompletion(
                request, response, null, null);

        verify(auditService).audit(
                eq("CARM-CORR-001"),
                eq("POST"),
                eq("/api/carm/fcm/facility"),
                eq("POST"),
                eq("SUCCESS"),
                any(),
                any(),
                any(),
                isNull(),
                anyMap());
    }

    @Test
    void shouldNotAuditNonApiRequest() {
        when(request.getRequestURI())
                .thenReturn("/actuator/health");

        interceptor.afterCompletion(
                request, response, null, null);

        verifyNoInteractions(auditService);
    }

    @Test
    void shouldNotAuditApiRequestWithoutCorrelationId() {
        when(request.getRequestURI())
                .thenReturn("/api/carm/fcm/facility");
        when(request.getHeader(
                ApiAuditInterceptor.CORRELATION_ID_HEADER))
                .thenReturn(null);

        interceptor.afterCompletion(
                request, response, null, null);

        verifyNoInteractions(auditService);
    }
}
