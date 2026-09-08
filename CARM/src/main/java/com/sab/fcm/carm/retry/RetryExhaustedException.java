package com.sab.fcm.carm.retry;

public class RetryExhaustedException extends Exception {

    public RetryExhaustedException(
            String message,
            Throwable cause) {
        super(message, cause);
    }
}
