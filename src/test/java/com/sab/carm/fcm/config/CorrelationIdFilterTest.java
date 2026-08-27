package com.sab.carm.fcm.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockFilterChain;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CorrelationIdFilterTest {

    private CorrelationIdFilter filter;

    @BeforeEach
    void setUp() {
        filter = new CorrelationIdFilter();
    }

    @Test
    void shouldRejectApiRequestWithoutCorrelationId()
            throws ServletException, IOException {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setRequestURI("/api/carm/fcm/facility");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        MockFilterChain filterChain =
                new MockFilterChain();

        filter.doFilterInternal(
                request,
                response,
                filterChain);

        assertEquals(400, response.getStatus());
        assertEquals(
                "application/json",
                response.getContentType());

        assertTrue(
                response.getContentAsString()
                        .contains("MISSING_CORRELATION_ID"));

        assertEquals(
                0,
                filterChain.getRequest() == null ? 0 : 1);
    }

    @Test
    void shouldRejectBlankCorrelationId()
            throws ServletException, IOException {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setRequestURI("/api/carm/fcm/facility");
        request.addHeader(
                CorrelationIdFilter.CORRELATION_ID_HEADER,
                "   ");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        MockFilterChain filterChain =
                new MockFilterChain();

        filter.doFilterInternal(
                request,
                response,
                filterChain);

        assertEquals(400, response.getStatus());
        assertEquals(
                "application/json",
                response.getContentType());

        assertTrue(
                response.getContentAsString()
                        .contains("MISSING_CORRELATION_ID"));

        assertEquals(
                0,
                filterChain.getRequest() == null ? 0 : 1);
    }

    @Test
    void shouldContinueApiRequestWithCorrelationId()
            throws ServletException, IOException {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setRequestURI("/api/carm/fcm/facility");
        request.addHeader(
                CorrelationIdFilter.CORRELATION_ID_HEADER,
                "CARM-001");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        MockFilterChain filterChain =
                new MockFilterChain();

        filter.doFilterInternal(
                request,
                response,
                filterChain);

        assertEquals(
                request,
                filterChain.getRequest());

        assertEquals(
                response,
                filterChain.getResponse());
    }

    @Test
    void shouldIgnoreNonApiRequest()
            throws ServletException, IOException {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setRequestURI("/actuator/health");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        MockFilterChain filterChain =
                new MockFilterChain();

        filter.doFilterInternal(
                request,
                response,
                filterChain);

        assertEquals(
                request,
                filterChain.getRequest());

        assertEquals(
                response,
                filterChain.getResponse());
    }
}
