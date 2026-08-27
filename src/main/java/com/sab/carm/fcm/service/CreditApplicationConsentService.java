package com.sab.carm.fcm.service;

import com.sab.carm.fcm.dto.integration.CreditApplicationConsentRequest;
import com.sab.carm.fcm.dto.integration.CreditApplicationConsentResponse;
import com.sab.carm.fcm.entity.CreditApplicationConsent;
import com.sab.carm.fcm.repository.CreditApplicationConsentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class CreditApplicationConsentService {

    private final CreditApplicationConsentRepository repository;

    public CreditApplicationConsentService(
            CreditApplicationConsentRepository repository) {
        this.repository = repository;
    }

    /**
     * Adds exactly one decision consent to the credit application's
     * consent array for each API request.
     *
     * Existing consents are never replaced.
     */
    public CreditApplicationConsentResponse addConsent(
            CreditApplicationConsentRequest request,
            String correlationId) {

        String transactionId = UUID.randomUUID().toString();

        Optional<CreditApplicationConsent> existing =
                repository.findByRelationshipIdAndSerialNo(
                        request.getRelationshipId(),
                        request.getSerialNo());

        CreditApplicationConsent document;

        if (existing.isPresent()) {
            document = existing.get();
        } else {
            document = new CreditApplicationConsent();
            document.setRelationshipId(request.getRelationshipId());
            document.setSerialNo(request.getSerialNo());
        }

        CreditApplicationConsent.Consent consent =
                new CreditApplicationConsent.Consent();

        consent.setDecision(request.getDecision().name());
        consent.setHubUserId(request.getHubUserId());
        consent.setConsentedAt(Instant.now());
        consent.setCorrelationId(correlationId);
        consent.setTransactionId(transactionId);

        document.getConsents().add(consent);

        CreditApplicationConsent saved =
                repository.save(document);

        CreditApplicationConsentResponse response =
                new CreditApplicationConsentResponse();

        response.setRelationshipId(saved.getRelationshipId());
        response.setSerialNo(saved.getSerialNo());
        response.setConsent(consent);

        return response;
    }
}
