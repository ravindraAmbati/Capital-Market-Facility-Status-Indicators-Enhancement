package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.config.IntegrationResponseHeaderFactory;
import com.sab.carm.fcm.dto.integration.DefaultsResponse;
import com.sab.carm.fcm.dto.integration.IntegrationResponse;
import com.sab.carm.fcm.service.MaintenanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carm/fcm")
@Tag(name = "CARM-FCM Defaults",
        description = "CARM-FCM integration maintenance/default data")
public class DefaultsController {

    public static final String CORRELATION_ID_HEADER =
            "X-CARM-FCM-CorrelationId";

    private final MaintenanceService maintenanceService;

    public DefaultsController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @Operation(
            summary = "Get complete maintenance defaults",
            description = "Returns all active facility-type and purpose-code maintenance "
                    + "values in one response. FCM exposes the maintenance data; "
                    + "CARM is responsible for consuming it and calculating defaults.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Maintenance defaults returned successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Missing or blank X-CARM-FCM-CorrelationId"),
            @ApiResponse(responseCode = "401",
                    description = "Authentication failed or bearer token is missing"),
            @ApiResponse(responseCode = "403",
                    description = "Caller is not authorized"),
            @ApiResponse(responseCode = "500",
                    description = "Unexpected server-side processing error")
    })
    @GetMapping(value = "/defaults",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public IntegrationResponse<DefaultsResponse> getDefaults(
            @Parameter(
                    name = CORRELATION_ID_HEADER,
                    description = "Mandatory CARM-supplied correlation ID used "
                            + "to trace the request across CARM, FCM, logs and audit.",
                    required = true,
                    in = ParameterIn.HEADER)
            @RequestHeader(CORRELATION_ID_HEADER) String correlationId) {

        DefaultsResponse body = maintenanceService.getDefaults();

        return new IntegrationResponse<>(
                IntegrationResponseHeaderFactory.success(correlationId),
                body);
    }
}
