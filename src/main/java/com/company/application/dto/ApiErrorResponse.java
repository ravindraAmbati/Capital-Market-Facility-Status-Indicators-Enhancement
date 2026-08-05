package com.company.application.dto;

import java.time.Instant;

/**
 * Standard error response returned by REST controllers.
 */
public class ApiErrorResponse {

    private final Instant timestamp;
    private final int status;
    private final String message;
    private final String path;

    public ApiErrorResponse(int status, String message, String path) {
        this.timestamp = Instant.now();
        this.status = status;
        this.message = message;
        this.path = path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
