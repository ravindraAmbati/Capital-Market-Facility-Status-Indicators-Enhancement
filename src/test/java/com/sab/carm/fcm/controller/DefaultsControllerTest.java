package com.sab.carm.fcm.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.sab.carm.fcm.config.CarmFcmTransactionContext;
import com.sab.carm.fcm.config.IntegrationResponseHeaderFactory;
import com.sab.carm.fcm.dto.integration.DefaultsResponse;
import com.sab.carm.fcm.dto.integration.IntegrationResponse;
import com.sab.carm.fcm.service.MaintenanceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class DefaultsControllerTest {

    @Mock
    private MaintenanceService maintenanceService;

    private DefaultsController controller;

    @BeforeEach
    void setUp() {
        controller = new DefaultsController(maintenanceService);
    }

    @AfterEach
    void tearDown() {
        CarmFcmTransactionContext.clear();
    }

    @Test
    void shouldReturnDefaultsWithCorrelationAndTransactionId() {

        DefaultsResponse defaults = new DefaultsResponse();

        when(maintenanceService.getDefaults())
                .thenReturn(defaults);

        ResponseEntity<IntegrationResponse<DefaultsResponse>> response =
                controller.getDefaults("CARM-CORR-001");

        assertNotNull(response);

        assertEquals(
                200,
                response.getStatusCodeValue());

        assertNotNull(response.getBody());

        IntegrationResponse<DefaultsResponse> body =
                response.getBody();

        assertNotNull(body.getHeader());

        assertEquals(
                "CARM-CORR-001",
                body.getHeader().getCorrelationId());

        assertNotNull(
                body.getHeader().getTransactionId());

        assertEquals(
                "SUCCESS",
                body.getHeader().getStatus());

        assertEquals(
                defaults,
                body.getBody());

        assertEquals(
                "CARM-CORR-001",
                response.getHeaders().getFirst(
                        IntegrationResponseHeaderFactory
                                .CORRELATION_ID_HEADER));

        assertNotNull(
                response.getHeaders().getFirst(
                        IntegrationResponseHeaderFactory
                                .TRANSACTION_ID_HEADER));
    }
}