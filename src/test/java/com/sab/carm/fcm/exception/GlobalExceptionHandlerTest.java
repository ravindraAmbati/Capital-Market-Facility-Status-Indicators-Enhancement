package com.sab.carm.fcm.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

class GlobalExceptionHandlerTest {

    @Test
    void returnsUnauthorizedForAuthFailures() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/security/authenticate");

        assertThat(new GlobalExceptionHandler()
                .unauthorized(new AuthenticationFailedException("Invalid credentials"), request)
                .getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
