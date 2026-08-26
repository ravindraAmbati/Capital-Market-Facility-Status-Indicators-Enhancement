package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.config.IntegrationResponseHeaderFactory;
import com.sab.carm.fcm.dto.integration.DefaultsResponse;
import com.sab.carm.fcm.dto.integration.IntegrationResponse;
import com.sab.carm.fcm.service.MaintenanceService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carm/fcm")
public class DefaultsController {

    private static final String CORRELATION_ID_HEADER =
            "X-CARM-FCM-CorrelationId";

    private final MaintenanceService maintenanceService;

    public DefaultsController(
            MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @GetMapping(
            value = "/defaults",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public IntegrationResponse<DefaultsResponse> getDefaults(
            @RequestHeader(CORRELATION_ID_HEADER) String correlationId) {

        DefaultsResponse body =
                maintenanceService.getDefaults();

        return new IntegrationResponse<>(
                IntegrationResponseHeaderFactory.success(
                        correlationId),
                body);
    }
}
