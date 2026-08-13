package com.sab.carm.fcm.dto;

/**
 * Simple success response for framework validation endpoints.
 */
public class ApiResponse {

    private final String message;

    public ApiResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
