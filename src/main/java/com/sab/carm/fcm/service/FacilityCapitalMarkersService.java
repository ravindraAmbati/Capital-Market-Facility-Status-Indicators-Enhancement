package com.sab.carm.fcm.service;

import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersResponse;
import com.sab.carm.fcm.entity.FacilityCapitalMarkers;
import com.sab.carm.fcm.repository.FacilityCapitalMarkersRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Read-only service for the CARM-FCM facility GET API.
 *
 * No create/update/delete/default processing belongs here.
 */
@Service
public class FacilityCapitalMarkersService {

    private final FacilityCapitalMarkersRepository repository;

    public FacilityCapitalMarkersService(FacilityCapitalMarkersRepository repository) {
        this.repository = repository;
    }

    public Optional<FacilityCapitalMarkersResponse> find(
            String relationshipId,
            String serialNo,
            String facilityNo) {

        return repository
                .findByCreditApplicationRelationshipIdAndSerialNoAndFacilityNo(
                        relationshipId, serialNo, facilityNo)
                .map(this::toResponse);
    }

    private FacilityCapitalMarkersResponse toResponse(
            FacilityCapitalMarkers entity) {

        FacilityCapitalMarkersResponse response =
                new FacilityCapitalMarkersResponse();

        response.setCreditApplicationRelationshipId(
                entity.getCreditApplicationRelationshipId());
        response.setSerialNo(entity.getSerialNo());
        response.setFacilityNo(entity.getFacilityNo());
        response.setCustomerId(entity.getCustomerId());
        response.setBorrowingGroup(entity.getBorrowingGroup());
        response.setProposalType(entity.getProposalType());
        response.setApplicationStatus(entity.getApplicationStatus());
        response.setFacilityType(entity.getFacilityType());
        response.setCarmPurposeCode(entity.getCarmPurposeCode());
        response.setStandingSecurityDocument(
                entity.getStandingSecurityDocument());
        response.setSeniorityType(entity.getSeniorityType());
        response.setUpdatedBy(entity.getUpdatedBy());
        response.setUpdatedDateTime(entity.getUpdatedDateTime());

        response.setAdvised(toRequestMarker(entity.getAdvised()));
        response.setCommitted(toRequestMarker(entity.getCommitted()));
        response.setUnconditionalCancellable(
                toRequestMarker(entity.getUnconditionalCancellable()));

        return response;
    }

    private com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersRequest.CapitalMarkerRequest
    toRequestMarker(FacilityCapitalMarkers.CapitalMarker source) {

        if (source == null) {
            return null;
        }

        com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersRequest.CapitalMarkerRequest
                target =
                new com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersRequest.CapitalMarkerRequest();

        target.setIndicator(source.getIndicator());
        target.setOverride(source.isOverride());
        target.setOverrideJustification(source.getOverrideJustification());

        return target;
    }

    /**
     * Internal compile-time adapter placeholder is deliberately unused.
     * Kept out of the public API surface.
     */
    private static class FacilityCapitalMarkersRequestMarkerAdapter {
        private String indicator;
        private boolean override;
        private String overrideJustification;
    }
}
