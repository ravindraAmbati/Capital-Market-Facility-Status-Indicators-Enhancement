package com.company.application.security;

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
}
