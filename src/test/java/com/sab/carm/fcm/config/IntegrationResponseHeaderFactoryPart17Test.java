package com.sab.carm.fcm.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationResponseHeaderFactoryPart17Test {

    @AfterEach
    void tearDown() {
        CarmFcmTransactionContext.clear();
    }

    @Test
    void shouldUseContextCorrelationAndTransactionIds() {

        CarmFcmTransactionContext.initialize(
                "CARM-001",
                "FCM-TXN-001");

        HttpHeaders headers =
                IntegrationResponseHeaderFactory
                        .httpHeaders("SHOULD-NOT-BE-USED");

        assertEquals(
                "CARM-001",
                headers.getFirst(
                        IntegrationResponseHeaderFactory
                                .CORRELATION_ID_HEADER));

        assertEquals(
                "FCM-TXN-001",
                headers.getFirst(
                        IntegrationResponseHeaderFactory
                                .TRANSACTION_ID_HEADER));
    }

    @Test
    void shouldUseSuppliedCorrelationWhenNoContextExists() {

        HttpHeaders headers =
                IntegrationResponseHeaderFactory
                        .httpHeaders("CARM-002");

        assertEquals(
                "CARM-002",
                headers.getFirst(
                        IntegrationResponseHeaderFactory
                                .CORRELATION_ID_HEADER));

        assertNotNull(
                headers.getFirst(
                        IntegrationResponseHeaderFactory
                                .TRANSACTION_ID_HEADER));
    }

    @Test
    void shouldGenerateTransactionIdWhenNoContextExists() {

        HttpHeaders first =
                IntegrationResponseHeaderFactory
                        .httpHeaders("CARM-003");

        HttpHeaders second =
                IntegrationResponseHeaderFactory
                        .httpHeaders("CARM-003");

        assertNotNull(first.getFirst(
                IntegrationResponseHeaderFactory
                        .TRANSACTION_ID_HEADER));

        assertNotNull(second.getFirst(
                IntegrationResponseHeaderFactory
                        .TRANSACTION_ID_HEADER));

        assertNotEquals(
                first.getFirst(
                        IntegrationResponseHeaderFactory
                                .TRANSACTION_ID_HEADER),
                second.getFirst(
                        IntegrationResponseHeaderFactory
                                .TRANSACTION_ID_HEADER));
    }

    @Test
    void responseHeadersShouldContainOnlyIntegrationHeaders() {

        CarmFcmTransactionContext.initialize(
                "CARM-004",
                "FCM-TXN-004");

        HttpHeaders headers =
                IntegrationResponseHeaderFactory
                        .httpHeaders("CARM-004");

        assertEquals(1,
                headers.get(
                        IntegrationResponseHeaderFactory
                                .CORRELATION_ID_HEADER).size());

        assertEquals(1,
                headers.get(
                        IntegrationResponseHeaderFactory
                                .TRANSACTION_ID_HEADER).size());
    }
}
