package com.company.application.entity;

import java.time.Instant;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * MongoDB audit record for security and request events.
 */
@Document(collection = "audit_records")
public class AuditRecord {

    @Id
    private String id;

    @Indexed
    private String eventType;

    @Indexed
    private String username;

    private String correlationId;
    private Instant timestamp;
    private Map<String, String> details;

    public AuditRecord() {
    }

    public AuditRecord(String eventType, String username, String correlationId, Map<String, String> details) {
        this.eventType = eventType;
        this.username = username;
        this.correlationId = correlationId;
        this.details = details;
        this.timestamp = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getEventType() {
        return eventType;
    }

    public String getUsername() {
        return username;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Map<String, String> getDetails() {
        return details;
    }
}
