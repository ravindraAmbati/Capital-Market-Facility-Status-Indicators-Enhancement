package com.sab.carm.fcm.service;

import com.sab.carm.fcm.dto.integration.CreditApplicationReportResponse;
import com.sab.carm.fcm.entity.CreditApplicationConsent;
import com.sab.carm.fcm.entity.FacilityCapitalMarkers;
import com.sab.carm.fcm.repository.CreditApplicationConsentRepository;
import com.sab.carm.fcm.repository.FacilityCapitalMarkersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditApplicationReportServiceTest {

    @Mock
    private FacilityCapitalMarkersRepository facilityRepository;

    @Mock
    private CreditApplicationConsentRepository consentRepository;

    private CreditApplicationReportService service;

    @BeforeEach
    void setUp() {
        service = new CreditApplicationReportService(
                facilityRepository,
                consentRepository);
    }

    @Test
    void shouldBuildReportFromFacilitiesAndConsents() {
        FacilityCapitalMarkers facility =
                new FacilityCapitalMarkers();
        facility.setCreditApplicationRelationshipId("REL001");
        facility.setSerialNo("001");
        facility.setFacilityNo("123");
        facility.setFacilityType("FT01");
        facility.setCarmPurposeCode("PUR01");

        FacilityCapitalMarkers.CapitalMarker advised =
                new FacilityCapitalMarkers.CapitalMarker();
        advised.setIndicator("Y");
        advised.setOverride(true);
        advised.setOverrideJustification("Business justification");
        facility.setAdvised(advised);

        CreditApplicationConsent consent =
                new CreditApplicationConsent();
        CreditApplicationConsent.Consent entry =
                new CreditApplicationConsent.Consent();
        entry.setDecision("RECOMMEND");
        entry.setHubUserId("AB12");
        entry.setCorrelationId("CARM-001");
        consent.getConsents().add(entry);

        when(facilityRepository
                .findByCreditApplicationRelationshipIdAndSerialNo(
                        "REL001", "001"))
                .thenReturn(Collections.singletonList(facility));

        when(consentRepository.findByRelationshipIdAndSerialNo(
                "REL001", "001"))
                .thenReturn(Optional.of(consent));

        CreditApplicationReportResponse response =
                service.getReport("REL001", "001");

        assertEquals("REL001", response.getRelationshipId());
        assertEquals("001", response.getSerialNo());
        assertEquals(1, response.getFacilities().size());
        assertEquals("123",
                response.getFacilities().get(0).getFacilityNo());
        assertEquals("Y",
                response.getFacilities().get(0).getAdvised());
        assertTrue(
                response.getFacilities().get(0).isAdvisedOverride());
        assertEquals(1, response.getConsents().size());
        assertEquals(
                "RECOMMEND",
                response.getConsents().get(0).getDecision());
    }

    @Test
    void shouldReturnEmptyReportWhenNoDataExists() {
        when(facilityRepository
                .findByCreditApplicationRelationshipIdAndSerialNo(
                        "REL001", "001"))
                .thenReturn(Collections.emptyList());

        when(consentRepository.findByRelationshipIdAndSerialNo(
                "REL001", "001"))
                .thenReturn(Optional.empty());

        CreditApplicationReportResponse response =
                service.getReport("REL001", "001");

        assertTrue(response.getFacilities().isEmpty());
        assertTrue(response.getConsents().isEmpty());
    }
}
