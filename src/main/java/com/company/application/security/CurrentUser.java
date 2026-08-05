package com.company.application.security;

import java.util.List;

/**
 * Current authenticated user details.
 */
public class CurrentUser {

    private final String username;
    private final List<String> roles;
    private final String authenticationType;

    public CurrentUser(String username, List<String> roles, String authenticationType) {
        this.username = username;
        this.roles = roles;
        this.authenticationType = authenticationType;
    }

    public String getUsername() { return username; }
    public List<String> getRoles() { return roles; }
    public String getAuthenticationType() { return authenticationType; }
}
