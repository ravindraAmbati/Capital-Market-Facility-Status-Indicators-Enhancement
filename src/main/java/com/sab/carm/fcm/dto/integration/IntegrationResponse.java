package com.sab.carm.fcm.dto.integration;

public class IntegrationResponse<T> {
    private IntegrationResponseHeader header;
    private T body;

    public IntegrationResponse() {
    }

    public IntegrationResponse(IntegrationResponseHeader header, T body) {
        this.header = header;
        this.body = body;
    }

    public IntegrationResponseHeader getHeader() { return header; }
    public void setHeader(IntegrationResponseHeader value) { this.header = value; }

    public T getBody() { return body; }
    public void setBody(T value) { this.body = value; }
}
