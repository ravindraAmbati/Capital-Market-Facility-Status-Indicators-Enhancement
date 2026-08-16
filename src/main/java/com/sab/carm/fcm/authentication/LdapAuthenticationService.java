package com.sab.carm.fcm.authentication;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Authenticates users with the configured JNDI LDAP flow.
 *
 * Authentication is performed in two steps:
 *
 * 1. Search LDAP for the user.
 * 2. Authenticate the supplied password against the user's DN.
 */
@Service
public class LdapAuthenticationService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    LdapAuthenticationService.class);

    private final LdapConnectionFactory connectionFactory;
    private final LdapSearchService searchService;

    public LdapAuthenticationService(
            LdapConnectionFactory connectionFactory,
            LdapSearchService searchService) {

        this.connectionFactory = connectionFactory;
        this.searchService = searchService;
    }

    public LdapAuthenticationResult authenticate(
            String username,
            String password) {

        if (username == null || username.trim().isEmpty()) {
            return LdapAuthenticationResult.userNotFound(username);
        }

        if (password == null || password.isEmpty()) {
            return LdapAuthenticationResult.invalidPassword(username);
        }

        DirContext searchContext = null;
        DirContext validationContext = null;

        try {

            /*
             * Step 1:
             * Find the user's Distinguished Name.
             */
            searchContext =
                    connectionFactory.createAnonymousContext();

            String distinguishedName =
                    searchService.findDistinguishedName(
                            searchContext,
                            username);

            if (distinguishedName == null) {

                LOGGER.info(
                        "LDAP user not found username={}",
                        username);

                return LdapAuthenticationResult.userNotFound(
                        username);
            }

            /*
             * Step 2:
             * Authenticate the supplied password against
             * the user's Distinguished Name.
             */
            try {

                validationContext =
                        connectionFactory
                                .createAuthenticatedContext(
                                        distinguishedName,
                                        password);

                if (validationContext != null) {

                    LOGGER.info(
                            "LDAP authentication successful username={}",
                            username);

                    return LdapAuthenticationResult.success(
                            username,
                            distinguishedName);
                }

                LOGGER.info(
                        "LDAP password authentication failed username={}",
                        username);

                return LdapAuthenticationResult.invalidPassword(
                        username);

            } catch (NamingException ex) {

                /*
                 * A failed bind normally means invalid credentials.
                 *
                 * We deliberately don't expose the LDAP exception
                 * to the caller or to the HTTP response.
                 */
                LOGGER.info(
                        "LDAP password authentication failed username={}",
                        username);

                return LdapAuthenticationResult.invalidPassword(
                        username);
            }

        } catch (NamingException ex) {

            LOGGER.error(
                    "LDAP error while authenticating username={}",
                    username,
                    ex);

            return LdapAuthenticationResult.ldapError(
                    username);

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
        } catch (NamingException ex) {

            LOGGER.debug(
                    "LDAP context close failed");
        }
    }
}