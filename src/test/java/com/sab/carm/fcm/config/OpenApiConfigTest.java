package com.sab.carm.fcm.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OpenApiConfigTest {

    @Test
    void shouldExposeBearerSecurityScheme() {

        assertNotNull(
                new OpenApiConfig()
                        .carmFcmOpenAPI()
                        .getComponents()
                        .getSecuritySchemes()
                        .get("bearerAuth"));
    }

    @Test
    void shouldUseCarmFcmCorrelationHeader() {

        assertEquals(
                "X-CARM-FCM-CorrelationId",
                OpenApiConfig.CORRELATION_ID_HEADER);
    }
}
