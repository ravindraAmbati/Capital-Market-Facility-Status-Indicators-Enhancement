package com.sab.carm.fcm.config;

import com.sab.carm.fcm.dto.integration.IntegrationResponseHeader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationResponseHeaderFactoryTest {

    @AfterEach
    void tearDown() {
        CarmFcmTransactionContext.clear();
    }

    @Test
    void shouldUseContextValues() {
        CarmFcmTransactionContext.initialize(
                "CARM-001", "FCM-TXN-001");

        IntegrationResponseHeader header =
                IntegrationResponseHeaderFactory.success(
                        "different");

        assertEquals("CARM-001", header.getCorrelationId());
        assertEquals("FCM-TXN-001", header.getTransactionId());
        assertEquals("SUCCESS", header.getStatus());
    }

    @Test
    void shouldCreateFallbackForDirectUnitInvocation() {
        IntegrationResponseHeader header =
                IntegrationResponseHeaderFactory.success(
                        "CARM-001");

        assertEquals("CARM-001", header.getCorrelationId());
        assertNotNull(header.getTransactionId());
        assertFalse(header.getTransactionId().isEmpty());
    }

    @Test
    void shouldUseSameTransactionForSuccessAndFailure() {
        CarmFcmTransactionContext.initialize(
                "CARM-001", "FCM-TXN-001");

        IntegrationResponseHeader success =
                IntegrationResponseHeaderFactory.success(
                        "CARM-001");
        IntegrationResponseHeader failure =
                IntegrationResponseHeaderFactory.failed(
                        "CARM-001");

        assertEquals(
                success.getTransactionId(),
                failure.getTransactionId());
    }
}
