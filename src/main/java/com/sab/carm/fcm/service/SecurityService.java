package com.sab.carm.fcm.service;

import com.sab.carm.fcm.audit.AuditEvent;
import com.sab.carm.fcm.audit.AuditService;
import com.sab.carm.fcm.authentication.LdapAuthenticationResult;
import com.sab.carm.fcm.authentication.LdapAuthenticationService;
import com.sab.carm.fcm.authorization.AuthorizationService;
import com.sab.carm.fcm.dto.*;
import com.sab.carm.fcm.exception.AuthenticationFailedException;
import com.sab.carm.fcm.security.*;
import com.sab.carm.fcm.validator.AuthenticationRequestValidator;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.util.stream.Collectors;

/**
 * Coordinates authentication, authorization and security audit events.
 */
@Service
public class SecurityService {

    private final LdapAuthenticationService ldapAuthenticationService;
    private final AuthorizationService authorizationService;
    private final TokenService tokenService;
    private final AuditService auditService;
    private final AuthenticationRequestValidator validator;
    private final SecurityContextService securityContextService;

    public SecurityService(
            LdapAuthenticationService ldapAuthenticationService,
            AuthorizationService authorizationService,
            TokenService tokenService,
            AuditService auditService,
            AuthenticationRequestValidator validator,
            SecurityContextService securityContextService) {

        this.ldapAuthenticationService = ldapAuthenticationService;
        this.authorizationService = authorizationService;
        this.tokenService = tokenService;
        this.auditService = auditService;
        this.validator = validator;
        this.securityContextService = securityContextService;
    }

    /**
     * Authenticates an API user and generates a bearer token.
     */
    public AuthenticationResponse authenticate(
            AuthenticationRequest request,
            HttpServletRequest servletRequest) {

        String username = usernameOf(request);
        String clientIp = clientIp(servletRequest);

        authenticateWithLdap(request, username, clientIp);

        if (!authorizationService.isApiUser(username)) {

            auditFailure(
                    username,
                    clientIp,
                    "USER_NOT_AUTHORIZED_FOR_API");

            throw new AuthenticationFailedException(
                    "Access denied");
        }

        String token =
                tokenService.generateToken(
                        username,
                        authorizationService.rolesFor(username),
                        servletRequest.getHeader("Origin"));

        auditService.record(
                AuditEvent.tokenGenerated(
                        username,
                        "API",
                        clientIp,
                        MDC.get("correlationId")));

        return new AuthenticationResponse(
                token,
                tokenService.expirationSeconds(),
                "Bearer");
    }

    /**
     * Authenticates an interactive user and creates a session.
     * <p>
     * Allowed roles:
     * ADMIN
     * AUDIT
     * ITSUP
     */
    public void login(
            AuthenticationRequest request,
            HttpServletRequest servletRequest) {

        String username = usernameOf(request);
        String clientIp = clientIp(servletRequest);

        authenticateWithLdap(request, username, clientIp);

        if (!isLoginAuthorized(username)) {

            auditFailure(
                    username,
                    clientIp,
                    "USER_NOT_AUTHORIZED_FOR_LOGIN");

            throw new AuthenticationFailedException(
                    "Access denied");
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorizationService
                                .rolesFor(username)
                                .stream()
                                .map(role ->
                                        new SimpleGrantedAuthority(
                                                "ROLE_" + role))
                                .collect(Collectors.toList()));

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        servletRequest.getSession(true);

        auditService.record(
                AuditEvent.loginSuccess(
                        username,
                        "API",
                        clientIp,
                        MDC.get("correlationId")));
    }

    /**
     * Common LDAP authentication flow used by both
     * API authentication and interactive login.
     */
    private LdapAuthenticationResult authenticateWithLdap(
            AuthenticationRequest request,
            String username, String clientIp) {

        if (!validator.isValid(request)) {

            auditFailure(
                    username,
                    clientIp,
                    "INVALID_REQUEST");

            throw new AuthenticationFailedException(
                    "Invalid credentials");
        }

        LdapAuthenticationResult result =
                ldapAuthenticationService.authenticate(
                        username,
                        request.getPassword());

        if (!result.isSuccessful()) {
            auditFailure(
                    username,
                    clientIp,
                    result.getStatus().name());

            throw new AuthenticationFailedException(
                    "Invalid credentials");
        }

        return result;
    }

    /**
     * Determines whether a user is allowed to use
     * the interactive login.
     */
    private boolean isLoginAuthorized(String username) {

        return authorizationService.isAdmin(username)
                || authorizationService.isAuditUser(username)
                || authorizationService.isItsupUser(username);
    }

    /**
     * Extracts username safely from the request.
     */
    private String usernameOf(AuthenticationRequest request) {

        return request == null
                ? "unknown"
                : request.getUsername();
    }

    /**
     * Writes a common authentication failure audit event.
     */
    private void auditFailure(
            String username,
            String clientIp,
            String reason) {

        auditService.record(
                AuditEvent.loginFailure(
                        username,
                        clientIp,
                        MDC.get("correlationId"),
                        reason));
    }

    public void logout(HttpServletRequest request) {

        CurrentUser user =
                securityContextService.currentUser();
        String clientIp = clientIp(request);

        String token =
                securityContextService.bearerToken(request);

        if (token != null) {
            tokenService.invalidateToken(token);
        }

        HttpSession session =
                request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        SecurityContextHolder.clearContext();

        auditService.record(AuditEvent.logout(
                user.getUsername(),
                "API",
                clientIp,
                MDC.get("correlationId")));
    }

    public TokenStatusResponse tokenStatus(
            HttpServletRequest request) {

        CurrentToken token =
                securityContextService.currentToken(request);

        return new TokenStatusResponse(
                token != null,
                token == null
                        ? null
                        : token.getExpiresAt());
    }

    public SessionStatusResponse sessionStatus(
            HttpServletRequest request) {

        CurrentSession session =
                securityContextService.currentSession(request);

        return session == null
                ? new SessionStatusResponse(
                null,
                null,
                null)
                : new SessionStatusResponse(
                session.getSessionId(),
                session.getCreatedAt(),
                session.getExpiresAt());
    }

    public SecurityProfileResponse profile(
            HttpServletRequest request) {

        CurrentUser user =
                securityContextService.currentUser();

        CurrentToken token =
                securityContextService.currentToken(request);

        CurrentSession session =
                securityContextService.currentSession(request);

        Instant tokenExpiry =
                token == null
                        ? null
                        : token.getExpiresAt();

        Instant sessionExpiry =
                session == null
                        ? null
                        : session.getExpiresAt();

        return new SecurityProfileResponse(
                user.getUsername(),
                user.getRoles(),
                user.getAuthenticationType(),
                Instant.now(),
                tokenExpiry,
                sessionExpiry);
    }
    private String clientIp(
            HttpServletRequest request) {

        String forwardedFor =
                request.getHeader("X-Forwarded-For");

        if (forwardedFor != null
                && !forwardedFor.trim().isEmpty()) {

            return forwardedFor
                    .split(",")[0]
                    .trim();
        }

        return request.getRemoteAddr();
    }
}