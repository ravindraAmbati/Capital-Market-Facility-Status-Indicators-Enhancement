package com.sab.carm.fcm.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.sab.carm.fcm.dto.integration.DefaultsResponse;
import com.sab.carm.fcm.dto.integration.IntegrationResponse;
import com.sab.carm.fcm.service.MaintenanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class DefaultsControllerTest {

    @Mock
    private MaintenanceService maintenanceService;

    private DefaultsController controller;

    @BeforeEach
    void setUp() {
        controller = new DefaultsController(maintenanceService);
    }

    @Test
    void shouldReturnDefaultsWithCorrelationAndTransactionId() {
        DefaultsResponse defaults = new DefaultsResponse();

        when(maintenanceService.getDefaults())
                .thenReturn(defaults);

        IntegrationResponse<DefaultsResponse> response =
                controller.getDefaults("CARM-CORR-001");

        assertNotNull(response);
        assertNotNull(response.getHeader());
        assertEquals(
                "CARM-CORR-001",
                response.getHeader().getCorrelationId());
        assertNotNull(response.getHeader().getTransactionId());
        assertEquals(
                "SUCCESS",
                response.getHeader().getStatus());
        assertEquals(defaults, response.getBody());
    }
}
