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
    void shouldCreateTransactionContextForApiRequest()
            throws ServletException, IOException {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setRequestURI(
                "/api/carm/fcm/facility");

        request.addHeader(
                CorrelationIdFilter.CORRELATION_ID_HEADER,
                "CARM-123");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        final String[] values =
                new String[2];

        MockFilterChain chain =
                new MockFilterChain() {
                    @Override
                    public void doFilter(
                            javax.servlet.ServletRequest req,
                            javax.servlet.ServletResponse res)
                            throws IOException, ServletException {

                        values[0] =
                                CarmFcmTransactionContext
                                        .getCorrelationId();

                        values[1] =
                                CarmFcmTransactionContext
                                        .getTransactionId();
                    }
                };

        filter.doFilterInternal(
                request,
                response,
                chain);

        // Context must be available while the request is executing.
        assertEquals(
                "CARM-123",
                values[0]);

        assertNotNull(values[1]);
        assertFalse(values[1].trim().isEmpty());

        // Context must be cleared after the request.
        assertNull(
                CarmFcmTransactionContext
                        .getCorrelationId());

        assertNull(
                CarmFcmTransactionContext
                        .getTransactionId());
    }

    @Test
    void shouldClearContextAfterRequest()
            throws ServletException, IOException {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setRequestURI(
                "/api/carm/fcm/facility");

        request.addHeader(
                CorrelationIdFilter.CORRELATION_ID_HEADER,
                "CARM-123");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        final String[] transactionId =
                new String[1];

        MockFilterChain chain =
                new MockFilterChain() {
                    @Override
                    public void doFilter(
                            javax.servlet.ServletRequest req,
                            javax.servlet.ServletResponse res)
                            throws IOException, ServletException {

                        transactionId[0] =
                                CarmFcmTransactionContext
                                        .getTransactionId();
                    }
                };

        filter.doFilterInternal(
                request,
                response,
                chain);

        assertNotNull(transactionId[0]);
        assertFalse(transactionId[0].trim().isEmpty());

        assertNull(
                CarmFcmTransactionContext
                        .getCorrelationId());

        assertNull(
                CarmFcmTransactionContext
                        .getTransactionId());
    }

    @Test
    void shouldCreateDifferentTransactionForDifferentRequests()
            throws ServletException, IOException {

        String[] first =
                execute("CARM-001");

        String[] second =
                execute("CARM-002");

        assertNotEquals(
                first[1],
                second[1]);
    }

    private String[] execute(
            String correlationId)
            throws ServletException, IOException {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setRequestURI(
                "/api/carm/fcm/facility");

        request.addHeader(
                CorrelationIdFilter.CORRELATION_ID_HEADER,
                correlationId);

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        final String[] values =
                new String[2];

        MockFilterChain chain =
                new MockFilterChain() {
                    @Override
                    public void doFilter(
                            javax.servlet.ServletRequest req,
                            javax.servlet.ServletResponse res)
                            throws IOException, ServletException {

                        values[0] =
                                CarmFcmTransactionContext
                                        .getCorrelationId();

                        values[1] =
                                CarmFcmTransactionContext
                                        .getTransactionId();
                    }
                };

        filter.doFilterInternal(
                request,
                response,
                chain);

        return values;
    }
}
