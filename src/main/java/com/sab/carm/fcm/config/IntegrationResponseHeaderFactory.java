package com.sab.carm.fcm.config;

import com.sab.carm.fcm.dto.integration.IntegrationResponseHeader;
import org.springframework.http.HttpHeaders;

import java.util.UUID;

public final class IntegrationResponseHeaderFactory {

    public static final String CORRELATION_ID_HEADER =
            "X-CARM-FCM-CorrelationId";

    public static final String TRANSACTION_ID_HEADER =
            "X-CARM-FCM-TransactionId";

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

    /**
     * Builds the HTTP headers for a CARM-FCM integration response.
     *
     * The correlation ID is always the CARM-supplied value held in the
     * transaction context. The transaction ID is generated once per
     * request by CorrelationIdFilter and reused for the whole request.
     */
    public static HttpHeaders httpHeaders(String correlationId) {

        HttpHeaders headers = new HttpHeaders();

        headers.set(
                CORRELATION_ID_HEADER,
                effectiveCorrelationId(correlationId));

        headers.set(
                TRANSACTION_ID_HEADER,
                effectiveTransactionId());

        return headers;
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
