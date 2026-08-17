package com.sab.carm.fcm.audit;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

/**
 * Common audit event used by application logging and MongoDB persistence.
 *
 * This object is the single audit representation.
 */
public class AuditEvent {

    private final String eventType;
    private final String result;
    private final String username;
    private final String role;
    private final String clientIp;
    private final Instant timestamp;
    private final String correlationId;
    private final String reason;
    private final Map<String, String> details;

    private AuditEvent(
            String eventType,
            String result,
            String username,
            String role,
            String clientIp,
            Instant timestamp,
            String correlationId,
            String reason,
            Map<String, String> details) {

        this.eventType = eventType;
        this.result = result;
        this.username = username;
        this.role = role;
        this.clientIp = clientIp;
        this.timestamp = timestamp;
        this.correlationId = correlationId;
        this.reason = reason;
        this.details = details == null
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(details);
    }

    public static AuditEvent loginSuccess(
            String username,
            String role,
            String clientIp,
            String correlationId) {

        return new AuditEvent(
                "LOGIN",
                "SUCCESS",
                username,
                role,
                clientIp,
                Instant.now(),
                correlationId,
                null,
                Collections.emptyMap());
    }

    public static AuditEvent loginFailure(
            String username,
            String clientIp,
            String correlationId,
            String reason) {

        return new AuditEvent(
                "LOGIN",
                "FAILURE",
                username,
                null,
                clientIp,
                Instant.now(),
                correlationId,
                reason,
                Collections.emptyMap());
    }

    public static AuditEvent tokenGenerated(
            String username,
            String role,
            String clientIp,
            String correlationId) {

        return new AuditEvent(
                "TOKEN_GENERATED",
                "SUCCESS",
                username,
                role,
                clientIp,
                Instant.now(),
                correlationId,
                null,
                Collections.emptyMap());
    }

    public static AuditEvent logout(
            String username,
            String role,
            String clientIp,
            String correlationId) {

        return new AuditEvent(
                "LOGOUT",
                "SUCCESS",
                username,
                role,
                clientIp,
                Instant.now(),
                correlationId,
                null,
                Collections.emptyMap());
    }

    public static AuditEvent invalidToken(
            String username,
            String role,
            String clientIp,
            String correlationId,
            String reason) {

        return new AuditEvent(
                "INVALID_TOKEN",
                "FAILURE",
                username,
                role,
                clientIp,
                Instant.now(),
                correlationId,
                reason,
                Collections.emptyMap());
    }

    public String getEventType() {
        return eventType;
    }

    public String getResult() {
        return result;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getClientIp() {
        return clientIp;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getReason() {
        return reason;
    }

    public Map<String, String> getDetails() {
        return details;
    }
}