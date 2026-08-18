package com.sab.carm.fcm.dto;

public class RefreshSummary {

    private int created;
    private int updated;
    private int archived;
    private int reactivated;
    private int unchanged;

    public void incrementCreated() {
        created++;
    }

    public void incrementUpdated() {
        updated++;
    }

    public void incrementArchived() {
        archived++;
    }

    public void incrementReactivated() {
        reactivated++;
    }

    public void incrementUnchanged() {
        unchanged++;
    }

    public int getCreated() {
        return created;
    }

    public int getUpdated() {
        return updated;
    }

    public int getArchived() {
        return archived;
    }

    public int getReactivated() {
        return reactivated;
    }

    public int getUnchanged() {
        return unchanged;
    }
}