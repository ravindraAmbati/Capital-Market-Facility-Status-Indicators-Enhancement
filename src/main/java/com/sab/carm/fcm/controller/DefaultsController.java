package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.dto.integration.DefaultsResponse;
import com.sab.carm.fcm.dto.integration.IntegrationResponse;
import com.sab.carm.fcm.dto.integration.IntegrationResponseHeader;
import com.sab.carm.fcm.service.MaintenanceService;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carm/fcm")
public class DefaultsController {

    private static final String CORRELATION_ID_HEADER =
            "X-CARM-FCM-CorrelationId";

    private final MaintenanceService maintenanceService;

    public DefaultsController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @GetMapping(
            value = "/defaults",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public IntegrationResponse<DefaultsResponse> getDefaults(
            @RequestHeader(CORRELATION_ID_HEADER) String correlationId) {

        DefaultsResponse body = maintenanceService.getDefaults();

        IntegrationResponseHeader header =
                new IntegrationResponseHeader(
                        correlationId,
                        UUID.randomUUID().toString(),
                        "SUCCESS");

        return new IntegrationResponse<>(header, body);
    }
}
