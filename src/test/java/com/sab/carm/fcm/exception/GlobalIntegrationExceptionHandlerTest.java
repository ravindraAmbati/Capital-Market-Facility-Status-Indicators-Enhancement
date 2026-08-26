package com.sab.carm.fcm.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

class GlobalIntegrationExceptionHandlerTest {

    private final GlobalIntegrationExceptionHandler handler =
            new GlobalIntegrationExceptionHandler();

    @Test
    void shouldReturnConsistentValidationError() {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.addHeader(
                "X-CARM-FCM-CorrelationId",
                "CARM-001");

        ResponseEntity<IntegrationErrorResponse> response =
                handler.illegalArgument(
                        new IllegalArgumentException(
                                "invalid indicator"),
                        request);

        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(
                "CARM-001",
                response.getBody()
                        .getHeader()
                        .getCorrelationId());
        assertEquals(
                "FAILED",
                response.getBody()
                        .getHeader()
                        .getStatus());
        assertEquals(
                "INVALID_REQUEST",
                response.getBody()
                        .getBody()
                        .getCode());
    }

    @Test
    void shouldHideUnexpectedExceptionMessage() {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.addHeader(
                "X-CARM-FCM-CorrelationId",
                "CARM-002");

        ResponseEntity<IntegrationErrorResponse> response =
                handler.unexpected(
                        new RuntimeException(
                                "internal mongo details"),
                        request);

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode());

        assertEquals(
                "INTERNAL_ERROR",
                response.getBody()
                        .getBody()
                        .getCode());

        assertEquals(
                "An unexpected error occurred",
                response.getBody()
                        .getBody()
                        .getMessage());

        assertEquals(
                "CARM-002",
                response.getBody()
                        .getHeader()
                        .getCorrelationId());
    }
}
