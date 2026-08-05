package com.company.application.exception;

import com.company.application.dto.ApiErrorResponse;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Converts exceptions to consistent API responses without stack traces.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({AuthenticationFailedException.class, InvalidTokenException.class})
    public ResponseEntity<ApiErrorResponse> unauthorized(RuntimeException ex, HttpServletRequest request) {
        return error(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ApiErrorResponse> security(SecurityException ex, HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(ex.getCode().getStatus(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(ex.getCode().getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> unexpected(Exception ex, HttpServletRequest request) {
        LOGGER.error("Unexpected application exception", ex);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected application error", request);
    }

    private ResponseEntity<ApiErrorResponse> error(HttpStatus status, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(new ApiErrorResponse(status.value(), message, request.getRequestURI()));
    }
}
