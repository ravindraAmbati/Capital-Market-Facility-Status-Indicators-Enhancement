package com.sab.carm.fcm.authentication;

import com.sab.carm.fcm.authorization.AuthorizationService;
import com.sab.carm.fcm.security.LdapAuthenticationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LdapAuthenticationProviderTest {

    @Mock
    private LdapAuthenticationService
            ldapAuthenticationService;

    @Mock
    private AuthorizationService
            authorizationService;

    private LdapAuthenticationProvider provider;

    @BeforeEach
    void setUp() {

        provider =
                new LdapAuthenticationProvider(
                        ldapAuthenticationService,
                        authorizationService);
    }

    @Test
    void shouldAuthenticateAuthorizedAdminUser() {

        String username =
                "sa-svc-carm-admin";

        String password =
                "password";

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        password);

        when(ldapAuthenticationService.authenticate(
                username,
                password))
                .thenReturn(
                        LdapAuthenticationResult.success(
                                username,
                                "cn=admin-user"));

        when(authorizationService.rolesFor(username))
                .thenReturn(
                        Collections.singletonList("ADMIN"));

        Authentication result =
                provider.authenticate(
                        authentication);

        assertNotNull(result);

        assertTrue(
                result.isAuthenticated());

        assertEquals(
                username,
                result.getName());

        assertTrue(
                result.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(
                                "ROLE_ADMIN"::equals));

        verify(
                ldapAuthenticationService)
                .authenticate(
                        username,
                        password);

        verify(
                authorizationService)
                .rolesFor(username);
    }

    @Test
    void shouldAuthenticateAuditUser() {

        String username =
                "sa-svc-carm-audit";

        String password =
                "password";

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        password);

        when(ldapAuthenticationService.authenticate(
                username,
                password))
                .thenReturn(
                        LdapAuthenticationResult.success(
                                username,
                                "cn=audit-user"));

        when(authorizationService.rolesFor(username))
                .thenReturn(
                        Collections.singletonList("AUDIT"));

        Authentication result =
                provider.authenticate(
                        authentication);

        assertTrue(
                result.isAuthenticated());

        assertTrue(
                result.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(
                                "ROLE_AUDIT"::equals));
    }

    @Test
    void shouldAuthenticateItsupUser() {

        String username =
                "sa-svc-carm-itsup";

        String password =
                "password";

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        password);

        when(ldapAuthenticationService.authenticate(
                username,
                password))
                .thenReturn(
                        LdapAuthenticationResult.success(
                                username,
                                "cn=itsup-user"));

        when(authorizationService.rolesFor(username))
                .thenReturn(
                        Collections.singletonList("ITSUP"));

        Authentication result =
                provider.authenticate(
                        authentication);

        assertTrue(
                result.isAuthenticated());

        assertTrue(
                result.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(
                                "ROLE_ITSUP"::equals));
    }

    @Test
    void shouldRejectInvalidLdapCredentials() {

        String username =
                "sa-svc-carm-admin";

        String password =
                "wrong-password";

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        password);

        when(ldapAuthenticationService.authenticate(
                username,
                password))
                .thenReturn(
                        LdapAuthenticationResult.invalidPassword(
                                username));

        assertThrows(
                BadCredentialsException.class,
                () -> provider.authenticate(
                        authentication));

        verify(
                ldapAuthenticationService)
                .authenticate(
                        username,
                        password);

        verifyNoInteractions(
                authorizationService);
    }

    @Test
    void shouldRejectUserNotFound() {

        String username =
                "unknown-user";

        String password =
                "password";

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        password);

        when(ldapAuthenticationService.authenticate(
                username,
                password))
                .thenReturn(
                        LdapAuthenticationResult.userNotFound(
                                username));

        assertThrows(
                BadCredentialsException.class,
                () -> provider.authenticate(
                        authentication));

        verifyNoInteractions(
                authorizationService);
    }

    @Test
    void shouldRejectLdapError() {

        String username =
                "sa-svc-carm-admin";

        String password =
                "password";

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        password);

        when(ldapAuthenticationService.authenticate(
                username,
                password))
                .thenReturn(
                        LdapAuthenticationResult.ldapError(
                                username));

        assertThrows(
                BadCredentialsException.class,
                () -> provider.authenticate(
                        authentication));

        verifyNoInteractions(
                authorizationService);
    }

    @Test
    void shouldRejectAuthenticatedButUnauthorizedUser() {

        String username =
                "some-valid-ldap-user";

        String password =
                "password";

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        password);

        when(ldapAuthenticationService.authenticate(
                username,
                password))
                .thenReturn(
                        LdapAuthenticationResult.success(
                                username,
                                "cn=some-user"));

        when(authorizationService.rolesFor(username))
                .thenReturn(
                        Collections.emptyList());

        assertThrows(
                BadCredentialsException.class,
                () -> provider.authenticate(
                        authentication));

        verify(
                ldapAuthenticationService)
                .authenticate(
                        username,
                        password);

        verify(
                authorizationService)
                .rolesFor(username);
    }

    @Test
    void shouldSupportUsernamePasswordAuthenticationToken() {

        assertTrue(
                provider.supports(
                        UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void shouldNotSupportOtherAuthenticationTypes() {

        assertTrue(
                !provider.supports(
                        org.springframework.security.authentication
                                .AnonymousAuthenticationToken.class));
    }
}