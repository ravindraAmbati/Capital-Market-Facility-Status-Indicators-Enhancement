package com.sab.carm.fcm.exception;

/**
 * Raised when user authentication fails.
 */
public class AuthenticationFailedException extends RuntimeException {

    public AuthenticationFailedException(String message) {
        super(message);
    }
}
