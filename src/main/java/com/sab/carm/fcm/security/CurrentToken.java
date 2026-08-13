package com.sab.carm.fcm.security;

import java.time.Instant;

/**
 * Current bearer token details.
 */
public class CurrentToken {

    private final String token;
    private final Instant expiresAt;

    public CurrentToken(String token, Instant expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public String getToken() { return token; }
    public Instant getExpiresAt() { return expiresAt; }
}
