package com.sab.carm.fcm.config;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CorrelationIdFilterScopeTest {

    @Test
    void maintenanceApiDoesNotRequireCarmCorrelationId()
            throws Exception {
        assertScope("/api/maintenance/facility-types", false);
    }

    @Test
    void securityApiDoesNotRequireCarmCorrelationId()
            throws Exception {
        assertScope("/api/security/profile", false);
    }

    @Test
    void facilityIntegrationApiRequiresCarmCorrelationId()
            throws Exception {
        assertScope("/api/carm/fcm/facility", true);
    }

    @Test
    void defaultsIntegrationApiRequiresCarmCorrelationId()
            throws Exception {
        assertScope("/api/carm/fcm/defaults", true);
    }

    @Test
    void reportIntegrationApiRequiresCarmCorrelationId()
            throws Exception {
        assertScope("/api/carm/fcm/report", true);
    }

    @Test
    void referenceRefreshRequiresCarmCorrelationId()
            throws Exception {
        assertScope(
                "/api/carm/reference-data/refresh/1200",
                true);
    }

    private void assertScope(
            String uri,
            boolean expected)
            throws Exception {

        CorrelationIdFilter filter =
                new CorrelationIdFilter();

        Method method =
                CorrelationIdFilter.class.getDeclaredMethod(
                        "isCarmFcmIntegrationRequest",
                        HttpServletRequest.class);

        method.setAccessible(true);

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn(uri);

        boolean actual =
                (boolean) method.invoke(filter, request);

        if (expected) {
            assertTrue(actual);
        } else {
            assertFalse(actual);
        }
    }
}
