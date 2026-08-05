package com.company.application.security;

import com.company.application.authentication.LdapAuthenticationService;
import com.company.application.authorization.AuthorizationService;
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
 * Spring Security adapter for JNDI LDAP authentication.
 */
@Component
public class LdapAuthenticationProvider implements AuthenticationProvider {

    private final LdapAuthenticationService ldapAuthenticationService;
    private final AuthorizationService authorizationService;

    public LdapAuthenticationProvider(LdapAuthenticationService ldapAuthenticationService,
            AuthorizationService authorizationService) {
        this.ldapAuthenticationService = ldapAuthenticationService;
        this.authorizationService = authorizationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());
        if (!ldapAuthenticationService.authenticate(username, password)) {
            throw new BadCredentialsException("Invalid credentials");
        }
        List<String> roles = authorizationService.rolesFor(username);
        if (roles.isEmpty()) {
            throw new BadCredentialsException("User is not authorized");
        }
        return new UsernamePasswordAuthenticationToken(username, null, roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList()));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
