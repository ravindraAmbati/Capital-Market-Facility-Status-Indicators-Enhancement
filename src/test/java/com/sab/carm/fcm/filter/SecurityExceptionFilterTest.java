package com.sab.carm.fcm.filter;

import static org.assertj.core.api.Assertions.assertThat;

import com.sab.carm.fcm.exception.SecurityErrorCode;
import com.sab.carm.fcm.exception.SecurityException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class SecurityExceptionFilterTest {

    @Test
    void writesSecurityErrorsAsJson() throws Exception {
        SecurityExceptionFilter filter = new SecurityExceptionFilter(new ObjectMapper().findAndRegisterModules());
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/sample");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = (servletRequest, servletResponse) -> {
            throw new SecurityException(SecurityErrorCode.INVALID_TOKEN, "Invalid bearer token");
        };

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(498);
        assertThat(response.getContentAsString()).contains("Invalid bearer token");
    }
}
