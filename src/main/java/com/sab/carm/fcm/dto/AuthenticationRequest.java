package com.sab.carm.fcm.dto;

import javax.validation.constraints.NotBlank;

/**
 * API credential request.
 */
public class AuthenticationRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
