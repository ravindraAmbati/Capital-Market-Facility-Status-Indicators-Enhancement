package com.sab.carm.fcm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sab.carm.fcm.audit.AuditEvent;
import com.sab.carm.fcm.audit.AuditService;
import com.sab.carm.fcm.authentication.LdapAuthenticationResult;
import com.sab.carm.fcm.authentication.LdapAuthenticationService;
import com.sab.carm.fcm.authorization.AuthorizationService;
import com.sab.carm.fcm.dto.AuthenticationRequest;
import com.sab.carm.fcm.dto.AuthenticationResponse;
import com.sab.carm.fcm.exception.AuthenticationFailedException;
import com.sab.carm.fcm.security.SecurityContextService;
import com.sab.carm.fcm.security.TokenService;
import com.sab.carm.fcm.validator.AuthenticationRequestValidator;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    private LdapAuthenticationService ldapAuthenticationService;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuditService auditService;

    @Mock
    private AuthenticationRequestValidator validator;

    @Mock
    private SecurityContextService securityContextService;

    @Mock
    private HttpServletRequest servletRequest;

    private SecurityService securityService;

    @BeforeEach
    void setUp() {

        securityService =
                new SecurityService(
                        ldapAuthenticationService,
                        authorizationService,
                        tokenService,
                        auditService,
                        validator,
                        securityContextService);
    }

    @Test
    void authenticateShouldGenerateTokenForApiUser() {

        AuthenticationRequest request =
                new AuthenticationRequest(
                        "sa-svc-carm-api",
                        "password");

        LdapAuthenticationResult result =
                LdapAuthenticationResult.success(
                        request.getUsername(),
                        "cn=api-user");

        when(validator.isValid(request))
                .thenReturn(true);

        when(ldapAuthenticationService.authenticate(
                request.getUsername(),
                request.getPassword()))
                .thenReturn(result);

        when(authorizationService.isApiUser(
                request.getUsername()))
                .thenReturn(true);

        when(authorizationService.rolesFor(
                request.getUsername()))
                .thenReturn(
                        Collections.singletonList("API"));

        when(tokenService.generateToken(
                eq(request.getUsername()),
                any(),
                any()))
                .thenReturn("token");

        when(tokenService.expirationSeconds())
                .thenReturn(1800L);

        AuthenticationResponse response =
                securityService.authenticate(
                        request,
                        servletRequest);

        assertEquals(
                "token",
                response.getToken());

        verify(auditService)
                .record(any(AuditEvent.class));
    }

    @Test
    void authenticateShouldRejectNonApiUser() {

        AuthenticationRequest request =
                new AuthenticationRequest(
                        "sa-svc-carm-audit",
                        "password");

        when(validator.isValid(request))
                .thenReturn(true);

        when(ldapAuthenticationService.authenticate(
                request.getUsername(),
                request.getPassword()))
                .thenReturn(
                        LdapAuthenticationResult.success(
                                request.getUsername(),
                                "cn=audit-user"));

        when(authorizationService.isApiUser(
                request.getUsername()))
                .thenReturn(false);

        assertThrows(
                AuthenticationFailedException.class,
                () -> securityService.authenticate(
                        request,
                        servletRequest));

        verify(auditService)
                .record(any(AuditEvent.class));
    }

    @Test
    void loginShouldAllowAdminUser() {

        AuthenticationRequest request =
                new AuthenticationRequest(
                        "sa-svc-carm-admin",
                        "password");

        when(validator.isValid(request))
                .thenReturn(true);

        when(ldapAuthenticationService.authenticate(
                request.getUsername(),
                request.getPassword()))
                .thenReturn(
                        LdapAuthenticationResult.success(
                                request.getUsername(),
                                "cn=admin-user"));

        when(authorizationService.isAdmin(
                request.getUsername()))
                .thenReturn(true);

        when(authorizationService.rolesFor(
                request.getUsername()))
                .thenReturn(
                        Collections.singletonList("ADMIN"));

        securityService.login(
                request,
                servletRequest);

        verify(auditService)
                .record(any(AuditEvent.class));
    }

    @Test
    void loginShouldRejectUnauthorizedUser() {

        AuthenticationRequest request =
                new AuthenticationRequest(
                        "unknown-user",
                        "password");

        when(validator.isValid(request))
                .thenReturn(true);

        when(ldapAuthenticationService.authenticate(
                request.getUsername(),
                request.getPassword()))
                .thenReturn(
                        LdapAuthenticationResult.success(
                                request.getUsername(),
                                "cn=unknown-user"));

        when(authorizationService.isAdmin(
                request.getUsername()))
                .thenReturn(false);

        when(authorizationService.isAuditUser(
                request.getUsername()))
                .thenReturn(false);

        when(authorizationService.isItsupUser(
                request.getUsername()))
                .thenReturn(false);

        assertThrows(
                AuthenticationFailedException.class,
                () -> securityService.login(
                        request,
                        servletRequest));

        verify(auditService)
                .record(any(AuditEvent.class));
    }

    @Test
    void loginShouldRejectInvalidLdapCredentials() {

        AuthenticationRequest request =
                new AuthenticationRequest(
                        "sa-svc-carm-admin",
                        "wrong-password");

        when(validator.isValid(request))
                .thenReturn(true);

        when(ldapAuthenticationService.authenticate(
                request.getUsername(),
                request.getPassword()))
                .thenReturn(
                        LdapAuthenticationResult.invalidPassword(
                                request.getUsername()));

        assertThrows(
                AuthenticationFailedException.class,
                () -> securityService.login(
                        request,
                        servletRequest));

        verify(auditService)
                .record(any(AuditEvent.class));
    }
}