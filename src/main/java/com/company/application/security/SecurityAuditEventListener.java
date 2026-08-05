package com.company.application.security;

import com.company.application.audit.AuditService;
import org.slf4j.MDC;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * Audits Spring Security authentication events.
 */
@Component
public class SecurityAuditEventListener {

    private final AuditService auditService;

    public SecurityAuditEventListener(AuditService auditService) {
        this.auditService = auditService;
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        auditService.record("LOGIN_SUCCESS", event.getAuthentication().getName(), MDC.get("correlationId"));
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        auditService.record("LOGIN_FAILURE", event.getAuthentication().getName(), MDC.get("correlationId"));
    }
}
