package com.sab.carm.fcm.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CorrelationIdFilterTransactionTest {

    private final CorrelationIdFilter filter =
            new CorrelationIdFilter();

    @AfterEach
    void tearDown() {
        CarmFcmTransactionContext.clear();
    }

    @Test
    void shouldExposeBothIdsDuringRequestAndClearAfterward()
            throws ServletException, IOException {

        MockHttpServletRequest request =
                new MockHttpServletRequest();
        request.setRequestURI("/api/carm/fcm/facility");
        request.addHeader(
                CorrelationIdFilter.CORRELATION_ID_HEADER,
                "CARM-123");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        final String[] ids = new String[2];

        MockFilterChain chain = new MockFilterChain() {
            @Override
            public void doFilter(
                    javax.servlet.ServletRequest req,
                    javax.servlet.ServletResponse res)
                    throws IOException, ServletException {
                ids[0] =
                        CarmFcmTransactionContext
                                .getCorrelationId();
                ids[1] =
                        CarmFcmTransactionContext
                                .getTransactionId();
            }
        };

        filter.doFilterInternal(
                request, response, chain);

        assertEquals("CARM-123", ids[0]);
        assertNotNull(ids[1]);
        assertFalse(ids[1].isEmpty());

        assertNull(
                CarmFcmTransactionContext
                        .getCorrelationId());
        assertNull(
                CarmFcmTransactionContext
                        .getTransactionId());
    }
}
