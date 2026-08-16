package com.sab.carm.fcm.security;

import com.sab.carm.fcm.authentication.LdapAuthenticationResult;
import com.sab.carm.fcm.authentication.LdapAuthenticationService;
import com.sab.carm.fcm.authorization.AuthorizationService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Spring Security adapter for LDAP authentication.
 *
 * Responsibilities:
 * 1. Extract username/password from Spring Security authentication request.
 * 2. Delegate LDAP authentication to LdapAuthenticationService.
 * 3. Resolve application roles.
 * 4. Return an authenticated Spring Security Authentication object.
 *
 * This class does not perform LDAP searches directly.
 */
@Component
public class LdapAuthenticationProvider
        implements AuthenticationProvider {

    private final LdapAuthenticationService ldapAuthenticationService;
    private final AuthorizationService authorizationService;

    public LdapAuthenticationProvider(
            LdapAuthenticationService ldapAuthenticationService,
            AuthorizationService authorizationService) {

        this.ldapAuthenticationService =
                ldapAuthenticationService;

        this.authorizationService =
                authorizationService;
    }

    @Override
    public Authentication authenticate(
            Authentication authentication)
            throws AuthenticationException {

        String username = authentication.getName();

        Object credentials =
                authentication.getCredentials();

        if (credentials == null) {
            throw new BadCredentialsException(
                    "Invalid credentials");
        }

        String password =
                credentials.toString();

        LdapAuthenticationResult result =
                ldapAuthenticationService.authenticate(
                        username,
                        password);

        if (!result.isSuccessful()) {
            throw new BadCredentialsException(
                    "Invalid credentials");
        }

        List<String> roles =
                authorizationService.rolesFor(username);

        if (roles.isEmpty()) {
            throw new BadCredentialsException(
                    "User is not authorized");
        }

        return createAuthenticatedToken(
                username,
                roles);
    }

    private Authentication createAuthenticatedToken(
            String username,
            List<String> roles) {

        List<SimpleGrantedAuthority> authorities =
                roles.stream()
                        .map(role ->
                                new SimpleGrantedAuthority(
                                        "ROLE_" + role))
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                authorities);
    }

    @Override
    public boolean supports(
            Class<?> authentication) {

        return UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication);
    }
}