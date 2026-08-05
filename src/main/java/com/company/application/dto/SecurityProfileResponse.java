package com.company.application.dto;

import java.time.Instant;
import java.util.List;

/**
 * Current authenticated user's security profile.
 */
public class SecurityProfileResponse {

    private final String username;
    private final List<String> roles;
    private final String authenticationType;
    private final Instant loginTime;
    private final Instant tokenExpiry;
    private final Instant sessionExpiry;

    public SecurityProfileResponse(String username, List<String> roles, String authenticationType,
            Instant loginTime, Instant tokenExpiry, Instant sessionExpiry) {
        this.username = username;
        this.roles = roles;
        this.authenticationType = authenticationType;
        this.loginTime = loginTime;
        this.tokenExpiry = tokenExpiry;
        this.sessionExpiry = sessionExpiry;
    }

    public String getUsername() { return username; }
    public List<String> getRoles() { return roles; }
    public String getAuthenticationType() { return authenticationType; }
    public Instant getLoginTime() { return loginTime; }
    public Instant getTokenExpiry() { return tokenExpiry; }
    public Instant getSessionExpiry() { return sessionExpiry; }
}
