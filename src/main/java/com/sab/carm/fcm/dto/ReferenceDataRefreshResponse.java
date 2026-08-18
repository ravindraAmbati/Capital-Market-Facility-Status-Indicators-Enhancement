package com.sab.carm.fcm.dto;

import java.util.ArrayList;
import java.util.List;

public class ReferenceDataRefreshResponse {

    private String status;
    private String tableName;
    private RefreshSummary summary;
    private List<RefreshDetail> details = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public RefreshSummary getSummary() {
        return summary;
    }

    public void setSummary(RefreshSummary summary) {
        this.summary = summary;
    }

    public List<RefreshDetail> getDetails() {
        return details;
    }

    public void setDetails(List<RefreshDetail> details) {
        this.details = details;
    }
}