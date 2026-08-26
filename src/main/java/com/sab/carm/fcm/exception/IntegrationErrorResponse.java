package com.sab.carm.fcm.exception;

import com.sab.carm.fcm.dto.integration.IntegrationResponseHeader;

public class IntegrationErrorResponse {

    private IntegrationResponseHeader header;
    private ErrorBody body;

    public IntegrationErrorResponse() {
    }

    public IntegrationErrorResponse(
            IntegrationResponseHeader header,
            ErrorBody body) {
        this.header = header;
        this.body = body;
    }

    public IntegrationResponseHeader getHeader() {
        return header;
    }

    public void setHeader(IntegrationResponseHeader value) {
        this.header = value;
    }

    public ErrorBody getBody() {
        return body;
    }

    public void setBody(ErrorBody value) {
        this.body = value;
    }

    public static class ErrorBody {

        private String code;
        private String message;

        public ErrorBody() {
        }

        public ErrorBody(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String value) {
            this.code = value;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String value) {
            this.message = value;
        }
    }
}
