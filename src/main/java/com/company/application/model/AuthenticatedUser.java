package com.company.application.model;

import java.util.List;

/**
 * Simple authenticated user model.
 */
public class AuthenticatedUser {

    private final String username;
    private final List<String> roles;

    public AuthenticatedUser(String username, List<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }
}
