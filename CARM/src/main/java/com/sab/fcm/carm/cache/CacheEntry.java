package com.sab.fcm.carm.cache;

import com.fasterxml.jackson.databind.JsonNode;

public class CacheEntry {

    private final JsonNode value;
    private final long createdAt;
    private final long softExpiry;
    private final long hardExpiry;

    private volatile long nextRetryAllowedAt;

    public CacheEntry(
            JsonNode value,
            long createdAt,
            long softExpiry,
            long hardExpiry) {

        this.value = value;
        this.createdAt = createdAt;
        this.softExpiry = softExpiry;
        this.hardExpiry = hardExpiry;
    }

    public JsonNode getValue() {
        return value;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public boolean isSoftExpired(long now) {
        return now >= softExpiry;
    }

    public boolean isHardExpired(long now) {
        return now >= hardExpiry;
    }

    public long getNextRetryAllowedAt() {
        return nextRetryAllowedAt;
    }

    public void setNextRetryAllowedAt(long value) {
        nextRetryAllowedAt = value;
    }
}
