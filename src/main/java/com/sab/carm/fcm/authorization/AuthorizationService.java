package com.sab.carm.fcm.authorization;

import com.sab.carm.fcm.constants.SecurityConstants;
import com.sab.carm.fcm.security.SecurityRoleProperties;
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

        if (contains(properties.getAdmin(), username)) {
            roles.add(SecurityConstants.ROLE_ADMIN);
        }

        if (contains(properties.getApi(), username)) {
            roles.add(SecurityConstants.ROLE_API);
        }

        if (contains(properties.getAudit(), username)) {
            roles.add(SecurityConstants.ROLE_AUDIT);
        }

        if (contains(properties.getItsup(), username)) {
            roles.add(SecurityConstants.ROLE_ITSUP);
        }

        return roles;
    }

    public boolean isAuthorized(String username) {
        return !rolesFor(username).isEmpty();
    }

    public boolean hasRole(String username, String role) {
        return rolesFor(username).contains(role);
    }

    public boolean isAdmin(String username) {
        return hasRole(username, SecurityConstants.ROLE_ADMIN);
    }

    public boolean isApiUser(String username) {
        return hasRole(username, SecurityConstants.ROLE_API);
    }

    public boolean isAuditUser(String username) {
        return hasRole(username, SecurityConstants.ROLE_AUDIT);
    }

    public boolean isItsupUser(String username) {
        return hasRole(username, SecurityConstants.ROLE_ITSUP);
    }

    public boolean hasPermission(String username, String permission) {

        if (SecurityConstants.PERMISSION_READ.equalsIgnoreCase(permission)) {
            return isAdmin(username)
                    || isApiUser(username)
                    || isAuditUser(username)
                    || isItsupUser(username);
        }

        if (SecurityConstants.PERMISSION_WRITE.equalsIgnoreCase(permission)) {
            return isAdmin(username)
                    || isApiUser(username);
        }

        if (SecurityConstants.PERMISSION_ADMIN.equalsIgnoreCase(permission)) {
            return isAdmin(username);
        }

        return false;
    }

    private boolean contains(List<String> users, String username) {

        if (username == null || users == null) {
            return false;
        }

        return users.stream()
                .anyMatch(username::equalsIgnoreCase);
    }
}