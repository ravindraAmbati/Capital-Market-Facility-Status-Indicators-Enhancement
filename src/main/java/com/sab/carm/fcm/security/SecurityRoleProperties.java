package com.sab.carm.fcm.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configurable username-to-role mapping.
 */
@ConfigurationProperties(prefix = "security.roles")
public class SecurityRoleProperties {

    private List<String> admin = new ArrayList<>();
    private List<String> api = new ArrayList<>();
    private List<String> readonly = new ArrayList<>();

    public List<String> getAdmin() {
        return admin;
    }

    public void setAdmin(List<String> admin) {
        this.admin = admin;
    }

    public List<String> getApi() {
        return api;
    }

    public void setApi(List<String> api) {
        this.api = api;
    }

    public List<String> getReadonly() {
        return readonly;
    }

    public void setReadonly(List<String> readonly) {
        this.readonly = readonly;
    }
}
