package com.company.application.service;

import com.company.application.audit.AuditService;
import com.company.application.authentication.LdapAuthenticationService;
import com.company.application.authorization.AuthorizationService;
import com.company.application.dto.AuthenticationRequest;
import com.company.application.dto.AuthenticationResponse;
import com.company.application.dto.SecurityProfileResponse;
import com.company.application.dto.SessionStatusResponse;
import com.company.application.dto.TokenStatusResponse;
import com.company.application.exception.AuthenticationFailedException;
import com.company.application.security.CurrentSession;
import com.company.application.security.CurrentToken;
import com.company.application.security.CurrentUser;
import com.company.application.security.SecurityContextService;
import com.company.application.security.TokenService;
import com.company.application.validator.AuthenticationRequestValidator;
import java.time.Instant;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Coordinates security APIs and security audit events.
 */
@Service
public class SecurityService {

    private final LdapAuthenticationService ldapAuthenticationService;
    private final AuthorizationService authorizationService;
    private final TokenService tokenService;
    private final AuditService auditService;
    private final AuthenticationRequestValidator validator;
    private final SecurityContextService securityContextService;

    public SecurityService(LdapAuthenticationService ldapAuthenticationService, AuthorizationService authorizationService,
            TokenService tokenService, AuditService auditService, AuthenticationRequestValidator validator,
            SecurityContextService securityContextService) {
        this.ldapAuthenticationService = ldapAuthenticationService;
        this.authorizationService = authorizationService;
        this.tokenService = tokenService;
        this.auditService = auditService;
        this.validator = validator;
        this.securityContextService = securityContextService;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletRequest servletRequest) {
        String username = request == null ? "unknown" : request.getUsername();
        if (!validator.isValid(request)
                || !ldapAuthenticationService.authenticate(request.getUsername(), request.getPassword())
                || !authorizationService.isApiUser(request.getUsername())) {
            auditService.record("LOGIN_FAILURE", username, MDC.get("correlationId"));
            throw new AuthenticationFailedException("Invalid credentials");
        }
        String token = tokenService.generateToken(request.getUsername(),
                authorizationService.rolesFor(request.getUsername()), servletRequest.getHeader("Origin"));
        auditService.record("TOKEN_GENERATED", request.getUsername(), MDC.get("correlationId"));
        return new AuthenticationResponse(token, tokenService.expirationSeconds(), "Bearer");
    }

    public void login(AuthenticationRequest request, HttpServletRequest servletRequest) {
        String username = request == null ? "unknown" : request.getUsername();
        if (!validator.isValid(request)
                || !ldapAuthenticationService.authenticate(request.getUsername(), request.getPassword())
                || (!authorizationService.isAdmin(request.getUsername()) && !authorizationService.isReadOnly(request.getUsername()))) {
            auditService.record("LOGIN_FAILURE", username, MDC.get("correlationId"));
            throw new AuthenticationFailedException("Invalid credentials");
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                request.getUsername(), null, authorizationService.rolesFor(request.getUsername()).stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        servletRequest.getSession(true);
        auditService.record("LOGIN_SUCCESS", request.getUsername(), MDC.get("correlationId"));
    }

    public void logout(HttpServletRequest request) {
        CurrentUser user = securityContextService.currentUser();
        String token = securityContextService.bearerToken(request);
        if (token != null) {
            tokenService.invalidateToken(token);
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        auditService.record("LOGOUT", user.getUsername(), MDC.get("correlationId"));
    }

    public TokenStatusResponse tokenStatus(HttpServletRequest request) {
        CurrentToken token = securityContextService.currentToken(request);
        return new TokenStatusResponse(token != null, token == null ? null : token.getExpiresAt());
    }

    public SessionStatusResponse sessionStatus(HttpServletRequest request) {
        CurrentSession session = securityContextService.currentSession(request);
        return session == null ? new SessionStatusResponse(null, null, null)
                : new SessionStatusResponse(session.getSessionId(), session.getCreatedAt(), session.getExpiresAt());
    }

    public SecurityProfileResponse profile(HttpServletRequest request) {
        CurrentUser user = securityContextService.currentUser();
        CurrentToken token = securityContextService.currentToken(request);
        CurrentSession session = securityContextService.currentSession(request);
        Instant tokenExpiry = token == null ? null : token.getExpiresAt();
        Instant sessionExpiry = session == null ? null : session.getExpiresAt();
        return new SecurityProfileResponse(user.getUsername(), user.getRoles(), user.getAuthenticationType(),
                Instant.now(), tokenExpiry, sessionExpiry);
    }
}
