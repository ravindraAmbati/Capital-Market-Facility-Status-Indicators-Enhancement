package com.company.application.dto;

import java.time.Instant;

/**
 * Current bearer token status.
 */
public class TokenStatusResponse {

    private final boolean active;
    private final Instant expiresAt;

    public TokenStatusResponse(boolean active, Instant expiresAt) {
        this.active = active;
        this.expiresAt = expiresAt;
    }

    public boolean isActive() { return active; }
    public Instant getExpiresAt() { return expiresAt; }
}
