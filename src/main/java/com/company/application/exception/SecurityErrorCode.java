package com.company.application.exception;

/**
 * Enterprise security error codes.
 */
public enum SecurityErrorCode {
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    LOGIN_TIMEOUT(440),
    INVALID_TOKEN(498);

    private final int status;

    SecurityErrorCode(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
