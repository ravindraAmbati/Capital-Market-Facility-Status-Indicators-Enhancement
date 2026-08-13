package com.sab.carm.fcm.exception;

/**
 * Raised when MongoDB infrastructure startup or persistence fails.
 */
public class MongoInfrastructureException extends RuntimeException {

    public MongoInfrastructureException(String message) {
        super(message);
    }

    public MongoInfrastructureException(String message, Throwable cause) {
        super(message, cause);
    }
}
