package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.dto.integration.CreditApplicationConsentRequest;
import com.sab.carm.fcm.dto.integration.CreditApplicationConsentResponse;
import com.sab.carm.fcm.dto.integration.IntegrationResponse;
import com.sab.carm.fcm.dto.integration.IntegrationResponseHeader;
import com.sab.carm.fcm.service.CreditApplicationConsentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

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
            @Valid @RequestBody CreditApplicationConsentRequest request) {

        CreditApplicationConsentResponse body =
                service.addConsent(request, correlationId);

        IntegrationResponseHeader header =
                new IntegrationResponseHeader(
                        correlationId,
                        UUID.randomUUID().toString(),
                        "SUCCESS");

        return ResponseEntity.ok(
                new IntegrationResponse<>(header, body));
    }
}
