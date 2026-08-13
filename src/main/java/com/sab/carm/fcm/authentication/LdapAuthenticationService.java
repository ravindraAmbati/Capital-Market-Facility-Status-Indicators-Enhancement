package com.sab.carm.fcm.authentication;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Authenticates users with the configured JNDI LDAP flow.
 */
@Service
public class LdapAuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LdapAuthenticationService.class);

    private final LdapConnectionFactory connectionFactory;
    private final LdapSearchService searchService;

    public LdapAuthenticationService(LdapConnectionFactory connectionFactory, LdapSearchService searchService) {
        this.connectionFactory = connectionFactory;
        this.searchService = searchService;
    }

    public boolean authenticate(String username, String password) {
        DirContext searchContext = null;
        DirContext validationContext = null;
        try {
            searchContext = connectionFactory.createAnonymousContext();
            String distinguishedName = searchService.findDistinguishedName(searchContext, username);
            if (distinguishedName == null) {
                return false;
            }
            validationContext = connectionFactory.createAuthenticatedContext(distinguishedName, password);
            return validationContext != null;
        } catch (NamingException ex) {
            LOGGER.warn("LDAP authentication failed for username={}", username);
            return false;
        } finally {
            close(validationContext);
            close(searchContext);
        }
    }

    private void close(DirContext context) {
        if (context == null) {
            return;
        }
        try {
            context.close();
        } catch (NamingException ignored) {
            LOGGER.debug("LDAP context close failed");
        }
    }
}
