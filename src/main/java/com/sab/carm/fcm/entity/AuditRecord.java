package com.sab.carm.fcm.entity;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * MongoDB audit record.
 *
 * Represents the same audit event written to the
 * application JSON log.
 */
@Document(collection = "audit_records")
public class AuditRecord {

    @Id
    private String id;

    @Indexed
    private String eventType;

    @Indexed
    private String result;

    @Indexed
    private String username;

    private String role;
    private String clientIp;
    private String correlationId;
    private Instant timestamp;
    private String reason;
    private Map<String, String> details;

    public AuditRecord() {
    }

    public AuditRecord(
            String eventType,
            String result,
            String username,
            String role,
            String clientIp,
            String correlationId,
            Instant timestamp,
            String reason,
            Map<String, String> details) {

        this.eventType = eventType;
        this.result = result;
        this.username = username;
        this.role = role;
        this.clientIp = clientIp;
        this.correlationId = correlationId;
        this.timestamp = timestamp;
        this.reason = reason;
        this.details = details == null
                ? Collections.emptyMap()
                : details;
    }

    public String getId() {
        return id;
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

    public String getCorrelationId() {
        return correlationId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getReason() {
        return reason;
    }

    public Map<String, String> getDetails() {
        return details;
    }
}