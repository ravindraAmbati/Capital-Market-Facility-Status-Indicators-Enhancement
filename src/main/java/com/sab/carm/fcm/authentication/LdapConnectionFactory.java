package com.sab.carm.fcm.authentication;

import com.sab.carm.fcm.security.LdapProperties;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.ldap.InitialLdapContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Creates JNDI LDAP contexts from configured properties.
 */
@Component
public class LdapConnectionFactory {

    private final LdapProperties properties;

    public LdapConnectionFactory(LdapProperties properties) {
        this.properties = properties;
    }

    public DirContext createAnonymousContext() throws NamingException {
        return new InitialLdapContext(baseEnvironment(), null);
    }

    public DirContext createAuthenticatedContext(String distinguishedName, String password) throws NamingException {
        Hashtable<String, String> environment = baseEnvironment();
        environment.put(Context.SECURITY_PRINCIPAL, distinguishedName);
        environment.put(Context.SECURITY_CREDENTIALS, password);
        return new InitialLdapContext(environment, null);
    }

    private Hashtable<String, String> baseEnvironment() {
        Hashtable<String, String> environment = new Hashtable<>();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, properties.getInitialContextFactory());
        environment.put(Context.PROVIDER_URL, properties.getProviderUrl());
        environment.put(Context.SECURITY_AUTHENTICATION, properties.getSecurityAuthentication());
        environment.put("com.sun.jndi.ldap.connect.timeout", String.valueOf(properties.getConnectionTimeoutMillis()));
        environment.put("com.sun.jndi.ldap.read.timeout", String.valueOf(properties.getReadTimeoutMillis()));
        if (StringUtils.hasText(properties.getSecurityProtocol())) {
            environment.put(Context.SECURITY_PROTOCOL, properties.getSecurityProtocol());
        }
        return environment;
    }
}
