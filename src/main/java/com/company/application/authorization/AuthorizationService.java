package com.company.application.authorization;

import com.company.application.constants.SecurityConstants;
import com.company.application.security.SecurityRoleProperties;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Resolves application roles from configured username lists.
 */
@Service
public class AuthorizationService {

    private final SecurityRoleProperties properties;

    public AuthorizationService(SecurityRoleProperties properties) {
        this.properties = properties;
    }

    public List<String> rolesFor(String username) {
        List<String> roles = new ArrayList<>();
        if (properties.getAdmin().contains(username)) {
            roles.add(SecurityConstants.ROLE_ADMIN);
        }
        if (properties.getApi().contains(username)) {
            roles.add(SecurityConstants.ROLE_API);
        }
        if (properties.getReadonly().contains(username)) {
            roles.add(SecurityConstants.ROLE_READONLY);
        }
        return roles;
    }

    public boolean hasRole(String username, String role) {
        return rolesFor(username).contains(role);
    }

    public boolean hasPermission(String username, String permission) {
        if ("WRITE".equalsIgnoreCase(permission)) {
            return isAdmin(username) || isApiUser(username);
        }
        if ("READ".equalsIgnoreCase(permission)) {
            return isAdmin(username) || isApiUser(username) || isReadOnly(username);
        }
        if ("ADMIN".equalsIgnoreCase(permission)) {
            return isAdmin(username);
        }
        return false;
    }

    public boolean isAdmin(String username) {
        return hasRole(username, SecurityConstants.ROLE_ADMIN);
    }

    public boolean isApiUser(String username) {
        return hasRole(username, SecurityConstants.ROLE_API);
    }

    public boolean isReadOnly(String username) {
        return hasRole(username, SecurityConstants.ROLE_READONLY);
    }
}
