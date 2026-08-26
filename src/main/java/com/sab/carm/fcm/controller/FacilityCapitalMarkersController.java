package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersResponse;
import com.sab.carm.fcm.dto.integration.IntegrationResponse;
import com.sab.carm.fcm.dto.integration.IntegrationResponseHeader;
import com.sab.carm.fcm.service.FacilityCapitalMarkersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * CARM-FCM Facility Capital Markers integration API.
 */
@RestController
@RequestMapping("/api/carm/fcm/facility")
public class FacilityCapitalMarkersController {

    public static final String CORRELATION_ID_HEADER =
            "X-CARM-FCM-CorrelationId";

    private final FacilityCapitalMarkersService service;

    public FacilityCapitalMarkersController(
            FacilityCapitalMarkersService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<IntegrationResponse<FacilityCapitalMarkersResponse>> get(
            @RequestHeader(CORRELATION_ID_HEADER) String correlationId,
            @RequestParam String relationshipId,
            @RequestParam String serialNo,
            @RequestParam String facilityNo) {

        return service.find(relationshipId, serialNo, facilityNo)
                .map(response -> ResponseEntity.ok(
                        new IntegrationResponse<>(
                                new IntegrationResponseHeader(
                                        correlationId,
                                        UUID.randomUUID().toString(),
                                        "SUCCESS"),
                                response)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .build());
    }
}
