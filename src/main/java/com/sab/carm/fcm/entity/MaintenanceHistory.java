package com.sab.carm.fcm.entity;

import java.time.Instant;
import java.util.Map;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "maintenanceHistory")
public class MaintenanceHistory extends BaseEntity {

    @Field("logicalTable")
    private String logicalTable;

    @Field("businessKey")
    private String businessKey;

    @Field("action")
    private String action;

    @Field("source")
    private String source;

    @Field("previousData")
    private Map<String, Object> previousData;

    @Field("newData")
    private Map<String, Object> newData;

    @Field("username")
    private String username;

    @Field("correlationId")
    private String correlationId;

    @Field("executedAt")
    private Instant executedAt;

    public String getLogicalTable() {
        return logicalTable;
    }

    public void setLogicalTable(String logicalTable) {
        this.logicalTable = logicalTable;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Map<String, Object> getPreviousData() {
        return previousData;
    }

    public void setPreviousData(Map<String, Object> previousData) {
        this.previousData = previousData;
    }

    public Map<String, Object> getNewData() {
        return newData;
    }

    public void setNewData(Map<String, Object> newData) {
        this.newData = newData;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public Instant getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(Instant executedAt) {
        this.executedAt = executedAt;
    }
}