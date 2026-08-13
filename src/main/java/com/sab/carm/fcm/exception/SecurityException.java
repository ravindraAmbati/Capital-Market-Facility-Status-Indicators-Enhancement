package com.sab.carm.fcm.exception;

/**
 * Standard security exception carrying enterprise status codes.
 */
public class SecurityException extends RuntimeException {

    private final SecurityErrorCode code;

    public SecurityException(SecurityErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public SecurityErrorCode getCode() {
        return code;
    }
}
