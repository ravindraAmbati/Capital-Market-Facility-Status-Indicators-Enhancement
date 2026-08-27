package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.config.ApiAuditRequestContext;
import com.sab.carm.fcm.config.IntegrationResponseHeaderFactory;
import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersOperationResponse;
import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersRequest;
import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersResponse;
import com.sab.carm.fcm.dto.integration.IntegrationResponse;
import com.sab.carm.fcm.service.FacilityCapitalMarkersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/carm/fcm/facility")
public class FacilityCapitalMarkersController {

    public static final String CORRELATION_ID_HEADER =
            IntegrationResponseHeaderFactory.CORRELATION_ID_HEADER;

    private final FacilityCapitalMarkersService service;

    public FacilityCapitalMarkersController(
            FacilityCapitalMarkersService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<
            IntegrationResponse<FacilityCapitalMarkersResponse>> get(
            @RequestHeader(CORRELATION_ID_HEADER) String correlationId,
            @RequestParam String relationshipId,
            @RequestParam String serialNo,
            @RequestParam String facilityNo,
            HttpServletRequest request) {

        ApiAuditRequestContext.setRelationshipId(request, relationshipId);
        ApiAuditRequestContext.setSerialNo(request, serialNo);
        ApiAuditRequestContext.setFacilityNo(request, facilityNo);

        return service.find(relationshipId, serialNo, facilityNo)
                .map(response -> ResponseEntity.ok()
                        .headers(IntegrationResponseHeaderFactory
                                .httpHeaders(correlationId))
                        .body(new IntegrationResponse<>(
                                IntegrationResponseHeaderFactory.success(
                                        correlationId),
                                response)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<
            IntegrationResponse<FacilityCapitalMarkersOperationResponse>> post(
            @RequestHeader(CORRELATION_ID_HEADER) String correlationId,
            @Valid @RequestBody FacilityCapitalMarkersRequest requestBody,
            HttpServletRequest request) {

        ApiAuditRequestContext.setRelationshipId(
                request, requestBody.getCreditApplicationRelationshipId());
        ApiAuditRequestContext.setSerialNo(
                request, requestBody.getSerialNo());
        ApiAuditRequestContext.setFacilityNo(
                request, requestBody.getFacilityNo());

        FacilityCapitalMarkersOperationResponse operation =
                service.upsert(requestBody, correlationId);

        return ResponseEntity.ok()
                .headers(IntegrationResponseHeaderFactory
                        .httpHeaders(correlationId))
                .body(new IntegrationResponse<>(
                        IntegrationResponseHeaderFactory.success(
                                correlationId),
                        operation));
    }

    @DeleteMapping
    public ResponseEntity<IntegrationResponse<Void>> delete(
            @RequestHeader(CORRELATION_ID_HEADER) String correlationId,
            @RequestParam String relationshipId,
            @RequestParam String serialNo,
            @RequestParam String facilityNo,
            HttpServletRequest request) {

        ApiAuditRequestContext.setRelationshipId(request, relationshipId);
        ApiAuditRequestContext.setSerialNo(request, serialNo);
        ApiAuditRequestContext.setFacilityNo(request, facilityNo);

        boolean deleted = service.delete(
                relationshipId,
                serialNo,
                facilityNo,
                correlationId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .headers(IntegrationResponseHeaderFactory
                        .httpHeaders(correlationId))
                .body(new IntegrationResponse<>(
                        IntegrationResponseHeaderFactory.success(
                                correlationId),
                        null));
    }
}
