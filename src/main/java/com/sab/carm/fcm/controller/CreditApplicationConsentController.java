package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.config.ApiAuditRequestContext;
import com.sab.carm.fcm.config.IntegrationResponseHeaderFactory;
import com.sab.carm.fcm.dto.integration.CreditApplicationConsentRequest;
import com.sab.carm.fcm.dto.integration.CreditApplicationConsentResponse;
import com.sab.carm.fcm.dto.integration.IntegrationResponse;
import com.sab.carm.fcm.service.CreditApplicationConsentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/carm/fcm/creditapplication")
public class CreditApplicationConsentController {

    public static final String CORRELATION_ID_HEADER =
            "X-CARM-FCM-CorrelationId";

    private final CreditApplicationConsentService service;

    public CreditApplicationConsentController(
            CreditApplicationConsentService service) {
        this.service = service;
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<
            IntegrationResponse<CreditApplicationConsentResponse>> post(
            @RequestHeader(CORRELATION_ID_HEADER) String correlationId,
            @Valid @RequestBody CreditApplicationConsentRequest requestBody,
            HttpServletRequest request) {

        ApiAuditRequestContext.setRelationshipId(
                request, requestBody.getRelationshipId());
        ApiAuditRequestContext.setSerialNo(
                request, requestBody.getSerialNo());
        ApiAuditRequestContext.setUserId(
                request, requestBody.getHubUserId());

        CreditApplicationConsentResponse body =
                service.addConsent(requestBody, correlationId);

        return ResponseEntity.ok(
                new IntegrationResponse<>(
                        IntegrationResponseHeaderFactory.success(
                                correlationId),
                        body));
    }
}
