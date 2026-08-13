package com.sab.carm.fcm.dto;

import java.time.Instant;

/**
 * Current HTTP session status.
 */
public class SessionStatusResponse {

    private final String sessionId;
    private final Instant createdAt;
    private final Instant expiresAt;

    public SessionStatusResponse(String sessionId, Instant createdAt, Instant expiresAt) {
        this.sessionId = sessionId;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getSessionId() { return sessionId; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getExpiresAt() { return expiresAt; }
}
