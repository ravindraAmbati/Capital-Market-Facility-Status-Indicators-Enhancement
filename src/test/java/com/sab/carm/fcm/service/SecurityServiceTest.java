package com.sab.carm.fcm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sab.carm.fcm.audit.AuditService;
import com.sab.carm.fcm.authentication.LdapAuthenticationService;
import com.sab.carm.fcm.authorization.AuthorizationService;
import com.sab.carm.fcm.dto.AuthenticationRequest;
import com.sab.carm.fcm.exception.AuthenticationFailedException;
import com.sab.carm.fcm.security.SecurityContextService;
import com.sab.carm.fcm.security.TokenService;
import com.sab.carm.fcm.security.CurrentUser;
import com.sab.carm.fcm.validator.AuthenticationRequestValidator;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

class SecurityServiceTest {

    @Test
    void authenticatesApiUserAndGeneratesToken() {
        LdapAuthenticationService ldap = mock(LdapAuthenticationService.class);
        AuthorizationService authorization = mock(AuthorizationService.class);
        TokenService tokenService = mock(TokenService.class);
        AuditService audit = mock(AuditService.class);
        AuthenticationRequestValidator validator = mock(AuthenticationRequestValidator.class);
        SecurityContextService context = mock(SecurityContextService.class);
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        AuthenticationRequest request = request();
        when(validator.isValid(request)).thenReturn(true);
        when(ldap.authenticate("svc_api", "secret")).thenReturn(true);
        when(authorization.isApiUser("svc_api")).thenReturn(true);
        when(authorization.rolesFor("svc_api")).thenReturn(Arrays.asList("API"));
        when(tokenService.generateToken(eq("svc_api"), any(), any())).thenReturn("token");
        when(tokenService.expirationSeconds()).thenReturn(1800L);

        SecurityService service = new SecurityService(ldap, authorization, tokenService, audit, validator, context);

        assertThat(service.authenticate(request, servletRequest).getToken()).isEqualTo("token");
        verify(audit).record(eq("TOKEN_GENERATED"), eq("svc_api"), any());
    }

    @Test
    void rejectsInvalidApiLogin() {
        SecurityService service = new SecurityService(mock(LdapAuthenticationService.class), mock(AuthorizationService.class),
                mock(TokenService.class), mock(AuditService.class), mock(AuthenticationRequestValidator.class),
                mock(SecurityContextService.class));

        assertThatThrownBy(() -> service.authenticate(request(), mock(HttpServletRequest.class)))
                .isInstanceOf(AuthenticationFailedException.class);
    }

    @Test
    void invalidatesSessionOnLogout() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        SecurityContextService context = mock(SecurityContextService.class);
        when(context.currentUser()).thenReturn(new CurrentUser("admin1",
                Arrays.asList("ADMIN"), "SESSION"));
        SecurityService service = new SecurityService(mock(LdapAuthenticationService.class), mock(AuthorizationService.class),
                mock(TokenService.class), mock(AuditService.class), mock(AuthenticationRequestValidator.class), context);

        service.logout(request);

        verify(session).invalidate();
    }

    private AuthenticationRequest request() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("svc_api");
        request.setPassword("secret");
        return request;
    }
}
