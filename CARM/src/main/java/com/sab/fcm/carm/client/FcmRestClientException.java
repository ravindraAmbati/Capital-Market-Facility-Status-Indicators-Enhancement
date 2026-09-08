package com.sab.fcm.carm.client;

public class FcmRestClientException extends RuntimeException {

    private final int statusCode;

    public FcmRestClientException(String message) {
        this(message, 0, null);
    }

    public FcmRestClientException(String message, Throwable cause) {
        this(message, 0, cause);
    }

    public FcmRestClientException(String message, int statusCode) {
        this(message, statusCode, null);
    }

    public FcmRestClientException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
