package com.sab.carm.fcm.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Document(collection = "apiAudit")
public class ApiAudit extends BaseEntity {

    @Field("correlationId")
    private String correlationId;

    @Field("transactionId")
    private String transactionId;

    @Field("httpMethod")
    private String httpMethod;

    @Field("apiPath")
    private String apiPath;

    @Field("operation")
    private String operation;

    @Field("status")
    private String status;

    @Field("relationshipId")
    private String relationshipId;

    @Field("serialNo")
    private String serialNo;

    @Field("facilityNo")
    private String facilityNo;

    @Field("userId")
    private String userId;

    @Field("timestamp")
    private Instant timestamp;

    @Field("details")
    private Map<String, Object> details = new LinkedHashMap<>();

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String value) {
        this.correlationId = value;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String value) {
        this.transactionId = value;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String value) {
        this.httpMethod = value;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String value) {
        this.apiPath = value;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String value) {
        this.operation = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public String getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(String value) {
        this.relationshipId = value;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String value) {
        this.serialNo = value;
    }

    public String getFacilityNo() {
        return facilityNo;
    }

    public void setFacilityNo(String value) {
        this.facilityNo = value;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String value) {
        this.userId = value;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant value) {
        this.timestamp = value;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> value) {
        this.details = value;
    }
}
