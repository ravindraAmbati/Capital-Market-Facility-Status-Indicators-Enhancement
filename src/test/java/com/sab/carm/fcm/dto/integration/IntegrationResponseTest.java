package com.sab.carm.fcm.dto.integration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegrationResponseTest {

    @Test
    void responseShouldContainHeaderAndBody() {
        IntegrationResponseHeader header =
                new IntegrationResponseHeader("CARM-001", "FCM-001", "SUCCESS");
        DefaultsResponse body = new DefaultsResponse();

        IntegrationResponse<DefaultsResponse> response =
                new IntegrationResponse<>(header, body);

        assertEquals("CARM-001", response.getHeader().getCorrelationId());
        assertEquals("FCM-001", response.getHeader().getTransactionId());
        assertEquals("SUCCESS", response.getHeader().getStatus());
        assertEquals(body, response.getBody());
    }
}
