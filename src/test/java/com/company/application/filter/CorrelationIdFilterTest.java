package com.company.application.filter;

import static org.assertj.core.api.Assertions.assertThat;

import javax.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class CorrelationIdFilterTest {

    @Test
    void createsCorrelationIdWhenMissing() throws Exception {
        CorrelationIdFilter filter = new CorrelationIdFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/actuator/health");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = (servletRequest, servletResponse) -> {
        };

        filter.doFilter(request, response, chain);

        assertThat(response.getHeader(CorrelationIdFilter.HEADER)).isNotBlank();
    }
}
