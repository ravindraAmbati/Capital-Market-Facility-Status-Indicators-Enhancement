package com.sab.carm.fcm.dto;

/**
 * Bearer token response.
 */
public class AuthenticationResponse {

    private final String token;
    private final long expiresIn;
    private final String tokenType;

    public AuthenticationResponse(String token, long expiresIn, String tokenType) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
    }

    public String getToken() {
        return token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }
}
