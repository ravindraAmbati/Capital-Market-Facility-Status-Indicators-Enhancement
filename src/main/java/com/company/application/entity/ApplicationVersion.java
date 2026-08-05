package com.company.application.entity;

import java.time.Instant;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Tracks applied MongoDB reference-data versions.
 */
@Document(collection = "applicationVersion")
public class ApplicationVersion extends BaseEntity {

    @Indexed(unique = true)
    @Field("version")
    private String databaseVersion;
    private Instant executedOn;
    private String executedBy;
    private String status;

    public ApplicationVersion() {
    }

    public ApplicationVersion(String version, String status) {
        this.databaseVersion = version;
        this.status = status;
        this.executedBy = "Application";
        this.executedOn = Instant.now();
    }

    public String getDatabaseVersion() { return databaseVersion; }
    public Instant getExecutedOn() { return executedOn; }
    public String getExecutedBy() { return executedBy; }
    public String getStatus() { return status; }
}
