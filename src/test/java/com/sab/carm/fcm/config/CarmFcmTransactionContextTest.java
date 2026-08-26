package com.sab.carm.fcm.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarmFcmTransactionContextTest {

    @AfterEach
    void tearDown() {
        CarmFcmTransactionContext.clear();
    }

    @Test
    void shouldStoreCorrelationAndTransactionId() {

        CarmFcmTransactionContext.initialize(
                "CARM-001",
                "FCM-001");

        assertEquals(
                "CARM-001",
                CarmFcmTransactionContext.getCorrelationId());

        assertEquals(
                "FCM-001",
                CarmFcmTransactionContext.getTransactionId());
    }

    @Test
    void shouldReturnNullWhenContextDoesNotExist() {

        assertNull(
                CarmFcmTransactionContext.getCorrelationId());

        assertNull(
                CarmFcmTransactionContext.getTransactionId());
    }

    @Test
    void shouldClearContext() {

        CarmFcmTransactionContext.initialize(
                "CARM-001",
                "FCM-001");

        CarmFcmTransactionContext.clear();

        assertNull(
                CarmFcmTransactionContext.getCorrelationId());

        assertNull(
                CarmFcmTransactionContext.getTransactionId());
    }

    @Test
    void shouldReplaceExistingContext() {

        CarmFcmTransactionContext.initialize(
                "CARM-001",
                "FCM-001");

        CarmFcmTransactionContext.initialize(
                "CARM-002",
                "FCM-002");

        assertEquals(
                "CARM-002",
                CarmFcmTransactionContext.getCorrelationId());

        assertEquals(
                "FCM-002",
                CarmFcmTransactionContext.getTransactionId());
    }
}
