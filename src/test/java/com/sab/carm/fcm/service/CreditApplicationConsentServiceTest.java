package com.sab.carm.fcm.service;

import com.sab.carm.fcm.dto.integration.CreditApplicationConsentRequest;
import com.sab.carm.fcm.dto.integration.CreditApplicationConsentResponse;
import com.sab.carm.fcm.dto.integration.DecisionType;
import com.sab.carm.fcm.entity.CreditApplicationConsent;
import com.sab.carm.fcm.repository.CreditApplicationConsentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditApplicationConsentServiceTest {

    @Mock
    private CreditApplicationConsentRepository repository;

    private CreditApplicationConsentService service;

    @BeforeEach
    void setUp() {
        service = new CreditApplicationConsentService(repository);
    }

    @Test
    void shouldCreateDocumentAndAddFirstConsent() {
        when(repository.findByRelationshipIdAndSerialNo(
                "REL001", "001"))
                .thenReturn(Optional.empty());

        when(repository.save(any(CreditApplicationConsent.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreditApplicationConsentResponse response =
                service.addConsent(
                        request(DecisionType.RECOMMEND, "AB12"),
                        "CARM-CORR-001");

        assertEquals("REL001", response.getRelationshipId());
        assertEquals("001", response.getSerialNo());
        assertEquals("RECOMMEND",
                response.getConsent().getDecision());
        assertEquals("AB12",
                response.getConsent().getHubUserId());
        assertEquals("CARM-CORR-001",
                response.getConsent().getCorrelationId());
        assertNotNull(response.getConsent().getTransactionId());
        assertNotNull(response.getConsent().getConsentedAt());

        verify(repository).save(argThat(document ->
                "REL001".equals(document.getRelationshipId())
                        && "001".equals(document.getSerialNo())
                        && document.getConsents().size() == 1));
    }

    @Test
    void shouldAppendConsentToExistingDocument() {
        CreditApplicationConsent existing =
                new CreditApplicationConsent();

        existing.setRelationshipId("REL001");
        existing.setSerialNo("001");

        CreditApplicationConsent.Consent previous =
                new CreditApplicationConsent.Consent();

        previous.setDecision("RECOMMEND");
        previous.setHubUserId("AB12");

        existing.getConsents().add(previous);

        when(repository.findByRelationshipIdAndSerialNo(
                "REL001", "001"))
                .thenReturn(Optional.of(existing));

        when(repository.save(any(CreditApplicationConsent.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreditApplicationConsentResponse response =
                service.addConsent(
                        request(DecisionType.APPROVE, "CD34"),
                        "CARM-CORR-002");

        assertEquals("APPROVE",
                response.getConsent().getDecision());

        verify(repository).save(argThat(document ->
                document == existing
                        && document.getConsents().size() == 2
                        && "RECOMMEND".equals(
                                document.getConsents().get(0).getDecision())
                        && "APPROVE".equals(
                                document.getConsents().get(1).getDecision())));
    }

    @Test
    void shouldCreateSeparateConsentForEveryRequest() {
        when(repository.findByRelationshipIdAndSerialNo(
                "REL001", "001"))
                .thenReturn(Optional.empty());

        when(repository.save(any(CreditApplicationConsent.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.addConsent(
                request(DecisionType.DECLINE, "AB12"),
                "CARM-CORR-003");

        verify(repository).save(argThat(document ->
                document.getConsents().size() == 1
                        && "DECLINE".equals(
                                document.getConsents().get(0).getDecision())));
    }

    private CreditApplicationConsentRequest request(
            DecisionType decision,
            String userId) {

        CreditApplicationConsentRequest request =
                new CreditApplicationConsentRequest();

        request.setRelationshipId("REL001");
        request.setSerialNo("001");
        request.setDecision(decision);
        request.setHubUserId(userId);

        return request;
    }
}
