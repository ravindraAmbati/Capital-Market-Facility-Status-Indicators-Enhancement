package com.sab.carm.fcm.service;

import com.sab.carm.fcm.dto.integration.CreditApplicationReportResponse;
import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersReportRow;
import com.sab.carm.fcm.entity.CreditApplicationConsent;
import com.sab.carm.fcm.entity.FacilityCapitalMarkers;
import com.sab.carm.fcm.repository.CreditApplicationConsentRepository;
import com.sab.carm.fcm.repository.FacilityCapitalMarkersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CreditApplicationReportService {

    private final FacilityCapitalMarkersRepository facilityRepository;
    private final CreditApplicationConsentRepository consentRepository;

    public CreditApplicationReportService(
            FacilityCapitalMarkersRepository facilityRepository,
            CreditApplicationConsentRepository consentRepository) {
        this.facilityRepository = facilityRepository;
        this.consentRepository = consentRepository;
    }

    public CreditApplicationReportResponse getReport(
            String relationshipId,
            String serialNo) {

        CreditApplicationReportResponse response =
                new CreditApplicationReportResponse();

        response.setRelationshipId(relationshipId);
        response.setSerialNo(serialNo);

        List<FacilityCapitalMarkersReportRow> facilities =
                facilityRepository
                        .findByCreditApplicationRelationshipIdAndSerialNo(
                                relationshipId, serialNo)
                        .stream()
                        .map(this::toRow)
                        .collect(Collectors.toList());

        response.setFacilities(facilities);

        Optional<CreditApplicationConsent> consent =
                consentRepository.findByRelationshipIdAndSerialNo(
                        relationshipId, serialNo);

        if (consent.isPresent()) {
            response.setConsents(
                    consent.get().getConsents());
        }

        return response;
    }

    private FacilityCapitalMarkersReportRow toRow(
            FacilityCapitalMarkers entity) {

        FacilityCapitalMarkersReportRow row =
                new FacilityCapitalMarkersReportRow();

        row.setRelationshipId(
                entity.getCreditApplicationRelationshipId());
        row.setSerialNo(entity.getSerialNo());
        row.setFacilityNo(entity.getFacilityNo());
        row.setCustomerId(entity.getCustomerId());
        row.setBorrowingGroup(entity.getBorrowingGroup());
        row.setProposalType(entity.getProposalType());
        row.setApplicationStatus(entity.getApplicationStatus());
        row.setFacilityType(entity.getFacilityType());
        row.setCarmPurposeCode(entity.getCarmPurposeCode());

        if (entity.getAdvised() != null) {
            row.setAdvised(entity.getAdvised().getIndicator());
            row.setAdvisedOverride(entity.getAdvised().isOverride());
            row.setAdvisedOverrideJustification(
                    entity.getAdvised().getOverrideJustification());
        }

        if (entity.getCommitted() != null) {
            row.setCommitted(entity.getCommitted().getIndicator());
            row.setCommittedOverride(entity.getCommitted().isOverride());
            row.setCommittedOverrideJustification(
                    entity.getCommitted().getOverrideJustification());
        }

        if (entity.getUnconditionalCancellable() != null) {
            row.setUnconditionalCancellable(
                    entity.getUnconditionalCancellable().getIndicator());
            row.setUnconditionalCancellableOverride(
                    entity.getUnconditionalCancellable().isOverride());
            row.setUnconditionalCancellableOverrideJustification(
                    entity.getUnconditionalCancellable()
                            .getOverrideJustification());
        }

        row.setStandingSecurityDocument(
                entity.getStandingSecurityDocument());
        row.setSeniorityType(entity.getSeniorityType());

        return row;
    }
}
