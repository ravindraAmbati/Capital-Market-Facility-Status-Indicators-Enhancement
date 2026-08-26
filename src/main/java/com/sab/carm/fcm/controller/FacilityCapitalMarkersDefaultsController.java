package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersDefaultsResponse;
import com.sab.carm.fcm.dto.integration.IntegrationResponse;
import com.sab.carm.fcm.dto.integration.IntegrationResponseHeader;
import com.sab.carm.fcm.service.FacilityCapitalMarkersDefaultsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/carm/fcm/defaults")
public class FacilityCapitalMarkersDefaultsController {

    public static final String CORRELATION_ID_HEADER =
            "X-CARM-FCM-CorrelationId";

    private final FacilityCapitalMarkersDefaultsService service;

    public FacilityCapitalMarkersDefaultsController(
            FacilityCapitalMarkersDefaultsService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<
            IntegrationResponse<FacilityCapitalMarkersDefaultsResponse>> getDefaults(
            @RequestHeader(CORRELATION_ID_HEADER) String correlationId) {

        FacilityCapitalMarkersDefaultsResponse defaults =
                service.findAll();

        return ResponseEntity.ok(
                new IntegrationResponse<>(
                        new IntegrationResponseHeader(
                                correlationId,
                                UUID.randomUUID().toString(),
                                "SUCCESS"),
                        defaults));
    }
}
