package com.sab.carm.fcm.config;

import com.sab.carm.fcm.dto.integration.IntegrationResponseHeader;

import java.util.UUID;

public final class IntegrationResponseHeaderFactory {

    private IntegrationResponseHeaderFactory() {
    }

    public static IntegrationResponseHeader success(
            String correlationId) {
        return new IntegrationResponseHeader(
                effectiveCorrelationId(correlationId),
                effectiveTransactionId(),
                "SUCCESS");
    }

    public static IntegrationResponseHeader failed(
            String correlationId) {
        return new IntegrationResponseHeader(
                effectiveCorrelationId(correlationId),
                effectiveTransactionId(),
                "FAILED");
    }

    private static String effectiveCorrelationId(
            String correlationId) {
        String contextValue =
                CarmFcmTransactionContext.getCorrelationId();
        return contextValue == null
                ? correlationId
                : contextValue;
    }

    private static String effectiveTransactionId() {
        String transactionId =
                CarmFcmTransactionContext.getTransactionId();
        return transactionId == null
                ? UUID.randomUUID().toString()
                : transactionId;
    }
}
