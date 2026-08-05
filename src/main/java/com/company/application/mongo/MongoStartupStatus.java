package com.company.application.mongo;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Captures Mongo startup initialization status for health checks.
 */
@Component
public class MongoStartupStatus {

    private boolean successful;
    private String referenceDataVersion = "none";
    private final List<String> initializedCollections = new ArrayList<>();

    public boolean isSuccessful() { return successful; }
    public void setSuccessful(boolean successful) { this.successful = successful; }
    public String getReferenceDataVersion() { return referenceDataVersion; }
    public void setReferenceDataVersion(String referenceDataVersion) { this.referenceDataVersion = referenceDataVersion; }
    public List<String> getInitializedCollections() { return initializedCollections; }
}
