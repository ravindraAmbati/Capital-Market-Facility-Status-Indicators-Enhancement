package com.company.application.exception;

/**
 * Raised when user authentication fails.
 */
public class AuthenticationFailedException extends RuntimeException {

    public AuthenticationFailedException(String message) {
        super(message);
    }
}
