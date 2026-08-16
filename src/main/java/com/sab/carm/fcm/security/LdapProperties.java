package com.sab.carm.fcm.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * LDAP connection properties used by the JNDI services.
 */
@ConfigurationProperties(prefix = "ldap")
public class LdapProperties {

    private String initialContextFactory;
    private String providerUrl;
    private String securityProtocol;
    private String securityAuthentication;
    private String peopleDirectory;
    private int connectionTimeoutMillis = 5000;
    private int readTimeoutMillis = 5000;
    private String bindUsername;
    private String bindPassword;

    public String getInitialContextFactory() {
        return initialContextFactory;
    }

    public void setInitialContextFactory(String initialContextFactory) {
        this.initialContextFactory = initialContextFactory;
    }

    public String getProviderUrl() {
        return providerUrl;
    }

    public void setProviderUrl(String providerUrl) {
        this.providerUrl = providerUrl;
    }

    public String getSecurityProtocol() {
        return securityProtocol;
    }

    public void setSecurityProtocol(String securityProtocol) {
        this.securityProtocol = securityProtocol;
    }

    public String getSecurityAuthentication() {
        return securityAuthentication;
    }

    public void setSecurityAuthentication(String securityAuthentication) {
        this.securityAuthentication = securityAuthentication;
    }

    public String getPeopleDirectory() {
        return peopleDirectory;
    }

    public void setPeopleDirectory(String peopleDirectory) {
        this.peopleDirectory = peopleDirectory;
    }

    public int getConnectionTimeoutMillis() {
        return connectionTimeoutMillis;
    }

    public void setConnectionTimeoutMillis(int connectionTimeoutMillis) {
        this.connectionTimeoutMillis = connectionTimeoutMillis;
    }

    public int getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    public void setReadTimeoutMillis(int readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }

    public String getBindPassword() {
        return bindPassword;
    }

    public void setBindPassword(String bindPassword) {
        this.bindPassword = bindPassword;
    }

    public String getBindUsername() {
        return bindUsername;
    }

    public void setBindUsername(String bindUsername) {
        this.bindUsername = bindUsername;
    }
}
