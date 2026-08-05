package com.company.application.controller;

import com.company.application.audit.AuditService;
import com.company.application.authentication.LdapAuthenticationService;
import com.company.application.authorization.AuthorizationService;
import com.company.application.constants.SecurityConstants;
import com.company.application.dto.AuthenticationRequest;
import com.company.application.dto.AuthenticationResponse;
import com.company.application.exception.AuthenticationFailedException;
import com.company.application.security.TokenService;
import com.company.application.validator.AuthenticationRequestValidator;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API authentication endpoints.
 */
@RestController
@RequestMapping("/api/security")
public class SecurityController {

    private final LdapAuthenticationService ldapAuthenticationService;
    private final AuthorizationService authorizationService;
    private final TokenService tokenService;
    private final AuditService auditService;
    private final AuthenticationRequestValidator validator;

    public SecurityController(LdapAuthenticationService ldapAuthenticationService,
            AuthorizationService authorizationService,
            TokenService tokenService,
            AuditService auditService,
            AuthenticationRequestValidator validator) {
        this.ldapAuthenticationService = ldapAuthenticationService;
        this.authorizationService = authorizationService;
        this.tokenService = tokenService;
        this.auditService = auditService;
        this.validator = validator;
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request, HttpServletRequest servletRequest) {
        if (!validator.isValid(request)
                || !ldapAuthenticationService.authenticate(request.getUsername(), request.getPassword())
                || !authorizationService.hasRole(request.getUsername(), SecurityConstants.ROLE_API)) {
            String username = request == null ? "unknown" : request.getUsername();
            auditService.record("FAILED_AUTHENTICATION", username, MDC.get("correlationId"));
            throw new AuthenticationFailedException("Invalid credentials");
        }
        String token = tokenService.generateToken(request.getUsername(),
                authorizationService.rolesFor(request.getUsername()), servletRequest.getHeader("Origin"));
        auditService.record("API_AUTHENTICATION", request.getUsername(), MDC.get("correlationId"));
        return new AuthenticationResponse(token, tokenService.expirationSeconds(), "Bearer");
    }
}
