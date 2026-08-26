package com.sab.carm.fcm.config;

import com.sab.carm.fcm.service.ApiAuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        interceptor =
                new ApiAuditInterceptor(auditService);
    }

    @Test
    void shouldClassifyFacilityGet() {
        verifyOperation(
                "GET",
                "/api/carm/fcm/facility",
                "FACILITY_GET");
    }

    @Test
    void shouldClassifyFacilityPost() {
        verifyOperation(
                "POST",
                "/api/carm/fcm/facility",
                "FACILITY_UPSERT");
    }

    @Test
    void shouldClassifyFacilityDelete() {
        verifyOperation(
                "DELETE",
                "/api/carm/fcm/facility",
                "FACILITY_DELETE");
    }

    @Test
    void shouldClassifyDefaultsGet() {
        verifyOperation(
                "GET",
                "/api/carm/fcm/defaults",
                "DEFAULTS_GET");
    }

    @Test
    void shouldClassifyConsentPost() {
        verifyOperation(
                "POST",
                "/api/carm/fcm/creditapplication",
                "CREDIT_APPLICATION_CONSENT");
    }

    @Test
    void shouldClassifyReportGet() {
        verifyOperation(
                "GET",
                "/api/carm/fcm/report",
                "REPORT_GET");
    }

    @Test
    void shouldClassifyUnknownApi() {
        verifyOperation(
                "PATCH",
                "/api/carm/fcm/unknown",
                "UNKNOWN_API_OPERATION");
    }

    @Test
    void shouldMarkFailedResponse() {

        when(request.getRequestURI())
                .thenReturn("/api/carm/fcm/facility");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader(
                ApiAuditInterceptor.CORRELATION_ID_HEADER))
                .thenReturn("CARM-001");
        when(response.getStatus()).thenReturn(404);

        interceptor.afterCompletion(
                request, response, null, null);

        ArgumentCaptor<String> status =
                ArgumentCaptor.forClass(String.class);

        verify(auditService).audit(
                eq("CARM-001"),
                eq("GET"),
                eq("/api/carm/fcm/facility"),
                eq("FACILITY_GET"),
                status.capture(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull());

        assertEquals("FAILED", status.getValue());
    }

    private void verifyOperation(
            String method,
            String path,
            String expectedOperation) {

        when(request.getRequestURI()).thenReturn(path);
        when(request.getMethod()).thenReturn(method);
        when(request.getHeader(
                ApiAuditInterceptor.CORRELATION_ID_HEADER))
                .thenReturn("CARM-001");
        when(response.getStatus()).thenReturn(200);

        interceptor.afterCompletion(
                request, response, null, null);

        verify(auditService).audit(
                eq("CARM-001"),
                eq(method),
                eq(path),
                eq(expectedOperation),
                eq("SUCCESS"),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull());
    }

    @Test
    void shouldNotAuditNonApiPath() {

        when(request.getRequestURI())
                .thenReturn("/actuator/health");

        interceptor.afterCompletion(
                request, response, null, null);

        verifyNoInteractions(auditService);
    }

    @Test
    void shouldNotAuditWithoutCorrelationId() {

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
