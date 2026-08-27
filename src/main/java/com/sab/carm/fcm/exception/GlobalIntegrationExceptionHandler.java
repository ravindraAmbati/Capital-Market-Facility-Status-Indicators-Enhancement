package com.sab.carm.fcm.exception;

import com.sab.carm.fcm.config.IntegrationResponseHeaderFactory;
import com.sab.carm.fcm.dto.integration.IntegrationResponseHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalIntegrationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<IntegrationErrorResponse> validation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField()
                        + ": "
                        + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return error(request, HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR", message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<IntegrationErrorResponse> illegalArgument(
            IllegalArgumentException exception,
            HttpServletRequest request) {

        return error(request, HttpStatus.BAD_REQUEST,
                "INVALID_REQUEST", exception.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<IntegrationErrorResponse> illegalState(
            IllegalStateException exception,
            HttpServletRequest request) {

        return error(request, HttpStatus.INTERNAL_SERVER_ERROR,
                "PROCESSING_ERROR", exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<IntegrationErrorResponse> unexpected(
            Exception exception,
            HttpServletRequest request) {

        return error(request, HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                "An unexpected error occurred");
    }

    private ResponseEntity<IntegrationErrorResponse> error(
            HttpServletRequest request,
            HttpStatus status,
            String code,
            String message) {

        String correlationId =
                request.getHeader(
                        IntegrationResponseHeaderFactory
                                .CORRELATION_ID_HEADER);

        IntegrationResponseHeader header =
                IntegrationResponseHeaderFactory.failed(
                        correlationId);

        return ResponseEntity
                .status(status)
                .headers(IntegrationResponseHeaderFactory
                        .httpHeaders(correlationId))
                .body(new IntegrationErrorResponse(
                        header,
                        new IntegrationErrorResponse.ErrorBody(
                                code,
                                message == null
                                        ? "Request failed"
                                        : message)));
    }
}
