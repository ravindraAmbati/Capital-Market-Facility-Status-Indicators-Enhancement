package com.company.application.security;

import java.time.Instant;
import java.util.List;

/**
 * Authenticated API token metadata.
 */
public class TokenDetails {

    private final String username;
    private final List<String> roles;
    private final Instant expiresAt;
    private final String origin;

    public TokenDetails(String username, List<String> roles, Instant expiresAt, String origin) {
        this.username = username;
        this.roles = roles;
        this.expiresAt = expiresAt;
        this.origin = origin;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public String getOrigin() {
        return origin;
    }
}
