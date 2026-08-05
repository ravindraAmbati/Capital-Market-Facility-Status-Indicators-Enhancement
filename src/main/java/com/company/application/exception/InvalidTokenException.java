package com.company.application.exception;

/**
 * Raised when a bearer token is missing, expired, or invalid.
 */
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }
}
