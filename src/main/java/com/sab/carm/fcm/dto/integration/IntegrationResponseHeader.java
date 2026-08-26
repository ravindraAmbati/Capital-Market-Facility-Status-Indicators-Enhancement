package com.sab.carm.fcm.dto.integration;

public class IntegrationResponseHeader {
    private String correlationId;
    private String transactionId;
    private String status;

    public IntegrationResponseHeader() {
    }

    public IntegrationResponseHeader(String correlationId, String transactionId, String status) {
        this.correlationId = correlationId;
        this.transactionId = transactionId;
        this.status = status;
    }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String value) { this.correlationId = value; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String value) { this.transactionId = value; }

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }
}
