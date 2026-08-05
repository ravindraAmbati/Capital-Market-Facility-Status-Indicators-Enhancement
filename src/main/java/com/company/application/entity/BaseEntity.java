package com.company.application.entity;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Common MongoDB entity fields for future business collections.
 */
public abstract class BaseEntity {

    @Id
    private String id;

    @Version
    @Field("entityVersion")
    private Long version;

    private Instant createdDate;
    private String createdBy;
    private Instant modifiedDate;
    private String modifiedBy;
    private boolean active = true;

    public String getId() { return id; }
    public Long getVersion() { return version; }
    public Instant getCreatedDate() { return createdDate; }
    public String getCreatedBy() { return createdBy; }
    public Instant getModifiedDate() { return modifiedDate; }
    public String getModifiedBy() { return modifiedBy; }
    public boolean isActive() { return active; }
    public void setCreatedDate(Instant createdDate) { this.createdDate = createdDate; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setModifiedDate(Instant modifiedDate) { this.modifiedDate = modifiedDate; }
    public void setModifiedBy(String modifiedBy) { this.modifiedBy = modifiedBy; }
    public void setActive(boolean active) { this.active = active; }
}
