package com.sab.carm.fcm.authentication;

/**
 * Result of LDAP authentication.
 */
public class LdapAuthenticationResult {

    public enum Status {
        SUCCESS,
        USER_NOT_FOUND,
        INVALID_PASSWORD,
        LDAP_ERROR
    }

    private final Status status;
    private final String username;
    private final String distinguishedName;

    private LdapAuthenticationResult(
            Status status,
            String username,
            String distinguishedName) {

        this.status = status;
        this.username = username;
        this.distinguishedName = distinguishedName;
    }

    public static LdapAuthenticationResult success(
            String username,
            String distinguishedName) {

        return new LdapAuthenticationResult(
                Status.SUCCESS,
                username,
                distinguishedName);
    }

    public static LdapAuthenticationResult userNotFound(
            String username) {

        return new LdapAuthenticationResult(
                Status.USER_NOT_FOUND,
                username,
                null);
    }

    public static LdapAuthenticationResult invalidPassword(
            String username) {

        return new LdapAuthenticationResult(
                Status.INVALID_PASSWORD,
                username,
                null);
    }

    public static LdapAuthenticationResult ldapError(
            String username) {

        return new LdapAuthenticationResult(
                Status.LDAP_ERROR,
                username,
                null);
    }

    public Status getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public boolean isSuccessful() {
        return Status.SUCCESS == status;
    }
}