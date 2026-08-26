package com.sab.carm.fcm.config;

import com.sab.carm.fcm.service.ApiAuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.*;
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
    void shouldAuditWithSameTransactionId() {

        when(request.getRequestURI())
                .thenReturn("/api/carm/fcm/facility");
        when(request.getMethod()).thenReturn("POST");
        when(request.getHeader(
                ApiAuditInterceptor.CORRELATION_ID_HEADER))
                .thenReturn("CARM-001");
        when(request.getAttribute(
                ApiAuditInterceptor.RELATIONSHIP_ID_ATTRIBUTE))
                .thenReturn("REL001");
        when(request.getAttribute(
                ApiAuditInterceptor.SERIAL_NO_ATTRIBUTE))
                .thenReturn("001");
        when(request.getAttribute(
                ApiAuditInterceptor.FACILITY_NO_ATTRIBUTE))
                .thenReturn("123");
        when(response.getStatus()).thenReturn(200);

        CarmFcmTransactionContext.initialize(
                "CARM-001",
                "FCM-TXN-001");

        interceptor.afterCompletion(
                request, response, null, null);

        verify(auditService).audit(
                eq("CARM-001"),
                eq("FCM-TXN-001"),
                eq("POST"),
                eq("/api/carm/fcm/facility"),
                eq("FACILITY_UPSERT"),
                eq("SUCCESS"),
                eq("REL001"),
                eq("001"),
                eq("123"),
                isNull(),
                isNull());

        CarmFcmTransactionContext.clear();
    }
}
