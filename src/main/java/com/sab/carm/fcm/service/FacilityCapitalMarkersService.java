package com.sab.carm.fcm.service;

import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersOperationResponse;
import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersRequest;
import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersResponse;
import com.sab.carm.fcm.dto.integration.FacilityOperation;
import com.sab.carm.fcm.entity.FacilityCapitalMarkers;
import com.sab.carm.fcm.entity.FacilityCapitalMarkersHistory;
import com.sab.carm.fcm.repository.FacilityCapitalMarkersHistoryRepository;
import com.sab.carm.fcm.repository.FacilityCapitalMarkersRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Facility Capital Markers service.
 *
 * POST is idempotent:
 * - no current record -> CREATE
 * - current record exists and differs -> UPDATE
 * - current record exists and is identical -> NO_CHANGE
 *
 * DELETE moves the complete current record to history, changes the historical
 * facility number to <facilityNo>_DELETED_<correlationId>, retains the original
 * facility number, and then removes the current record.
 */
@Service
public class FacilityCapitalMarkersService {

    private final FacilityCapitalMarkersRepository repository;
    private final FacilityCapitalMarkersHistoryRepository historyRepository;

    public FacilityCapitalMarkersService(
            FacilityCapitalMarkersRepository repository,
            FacilityCapitalMarkersHistoryRepository historyRepository) {
        this.repository = repository;
        this.historyRepository = historyRepository;
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

    public FacilityCapitalMarkersOperationResponse upsert(
            FacilityCapitalMarkersRequest request,
            String correlationId) {

        Optional<FacilityCapitalMarkers> existing =
                repository
                        .findByCreditApplicationRelationshipIdAndSerialNoAndFacilityNo(
                                request.getCreditApplicationRelationshipId(),
                                request.getSerialNo(),
                                request.getFacilityNo());

        if (!existing.isPresent()) {
            FacilityCapitalMarkers created =
                    toEntity(request, correlationId);

            FacilityCapitalMarkers saved =
                    repository.save(created);

            return operation(FacilityOperation.CREATED, saved);
        }

        FacilityCapitalMarkers current = existing.get();

        if (sameBusinessData(current, request)) {
            return operation(FacilityOperation.NO_CHANGE, current);
        }

        String transactionId = UUID.randomUUID().toString();

        FacilityCapitalMarkersHistory history =
                toHistory(current, correlationId, transactionId);

        history.setAction("UPDATE");
        historyRepository.save(history);

        updateEntity(current, request, correlationId);

        FacilityCapitalMarkers saved =
                repository.save(current);

        return operation(FacilityOperation.UPDATED, saved);
    }

    public boolean delete(
            String relationshipId,
            String serialNo,
            String facilityNo,
            String correlationId) {

        Optional<FacilityCapitalMarkers> existing =
                repository
                        .findByCreditApplicationRelationshipIdAndSerialNoAndFacilityNo(
                                relationshipId, serialNo, facilityNo);

        if (!existing.isPresent()) {
            return false;
        }

        FacilityCapitalMarkers current = existing.get();
        String transactionId = UUID.randomUUID().toString();

        FacilityCapitalMarkersHistory history =
                toHistory(current, correlationId, transactionId);

        history.setFacilityNo(
                facilityNo + "_DELETED_" + correlationId);
        history.setOriginalFacilityNo(facilityNo);
        history.setAction("DELETE");

        historyRepository.save(history);

        repository.delete(current);

        return true;
    }

    private void updateEntity(
            FacilityCapitalMarkers entity,
            FacilityCapitalMarkersRequest request,
            String correlationId) {

        entity.setCreditApplicationRelationshipId(
                request.getCreditApplicationRelationshipId());
        entity.setSerialNo(request.getSerialNo());
        entity.setFacilityNo(request.getFacilityNo());
        entity.setCustomerId(request.getCustomerId());
        entity.setBorrowingGroup(request.getBorrowingGroup());
        entity.setProposalType(request.getProposalType());
        entity.setApplicationStatus(request.getApplicationStatus());
        entity.setFacilityType(request.getFacilityType());
        entity.setCarmPurposeCode(request.getCarmPurposeCode());
        entity.setAdvised(toEntityMarker(request.getAdvised()));
        entity.setCommitted(toEntityMarker(request.getCommitted()));
        entity.setUnconditionalCancellable(
                toEntityMarker(request.getUnconditionalCancellable()));
        entity.setStandingSecurityDocument(
                request.getStandingSecurityDocument());
        entity.setSeniorityType(request.getSeniorityType());
        entity.setUpdatedBy(request.getUpdatedBy());
        entity.setUpdatedDateTime(request.getUpdatedDateTime());
        entity.setCorrelationId(correlationId);
    }

    private FacilityCapitalMarkers toEntity(
            FacilityCapitalMarkersRequest request,
            String correlationId) {

        FacilityCapitalMarkers entity =
                new FacilityCapitalMarkers();

        updateEntity(entity, request, correlationId);
        return entity;
    }

    private FacilityCapitalMarkers.CapitalMarker toEntityMarker(
            FacilityCapitalMarkersRequest.CapitalMarkerRequest source) {

        if (source == null) {
            return null;
        }

        FacilityCapitalMarkers.CapitalMarker target =
                new FacilityCapitalMarkers.CapitalMarker();

        target.setIndicator(source.getIndicator());
        target.setOverride(source.isOverride());
        target.setOverrideJustification(
                source.getOverrideJustification());

        return target;
    }

    private FacilityCapitalMarkersHistory toHistory(
            FacilityCapitalMarkers current,
            String correlationId,
            String transactionId) {

        FacilityCapitalMarkersHistory history =
                new FacilityCapitalMarkersHistory();

        history.setCreditApplicationRelationshipId(
                current.getCreditApplicationRelationshipId());
        history.setSerialNo(current.getSerialNo());
        history.setFacilityNo(current.getFacilityNo());
        history.setOriginalFacilityNo(current.getFacilityNo());
        history.setCustomerId(current.getCustomerId());
        history.setBorrowingGroup(current.getBorrowingGroup());
        history.setProposalType(current.getProposalType());
        history.setApplicationStatus(current.getApplicationStatus());
        history.setFacilityType(current.getFacilityType());
        history.setCarmPurposeCode(current.getCarmPurposeCode());
        history.setAdvised(current.getAdvised());
        history.setCommitted(current.getCommitted());
        history.setUnconditionalCancellable(
                current.getUnconditionalCancellable());
        history.setStandingSecurityDocument(
                current.getStandingSecurityDocument());
        history.setSeniorityType(current.getSeniorityType());
        history.setUpdatedBy(current.getUpdatedBy());
        history.setUpdatedDateTime(current.getUpdatedDateTime());
        history.setCorrelationId(correlationId);
        history.setTransactionId(transactionId);

        return history;
    }

    private boolean sameBusinessData(
            FacilityCapitalMarkers current,
            FacilityCapitalMarkersRequest request) {

        return equal(current.getCreditApplicationRelationshipId(),
                request.getCreditApplicationRelationshipId())
                && equal(current.getSerialNo(), request.getSerialNo())
                && equal(current.getFacilityNo(), request.getFacilityNo())
                && equal(current.getCustomerId(), request.getCustomerId())
                && equal(current.getBorrowingGroup(), request.getBorrowingGroup())
                && equal(current.getProposalType(), request.getProposalType())
                && equal(current.getApplicationStatus(), request.getApplicationStatus())
                && equal(current.getFacilityType(), request.getFacilityType())
                && equal(current.getCarmPurposeCode(), request.getCarmPurposeCode())
                && sameMarker(current.getAdvised(), request.getAdvised())
                && sameMarker(current.getCommitted(), request.getCommitted())
                && sameMarker(current.getUnconditionalCancellable(),
                              request.getUnconditionalCancellable())
                && equal(current.getStandingSecurityDocument(),
                          request.getStandingSecurityDocument())
                && equal(current.getSeniorityType(), request.getSeniorityType());
    }

    private boolean sameMarker(
            FacilityCapitalMarkers.CapitalMarker current,
            FacilityCapitalMarkersRequest.CapitalMarkerRequest request) {

        if (current == null || request == null) {
            return current == null && request == null;
        }

        return equal(current.getIndicator(), request.getIndicator())
                && current.isOverride() == request.isOverride()
                && equal(current.getOverrideJustification(),
                         request.getOverrideJustification());
    }

    private boolean equal(Object left, Object right) {
        return left == null ? right == null : left.equals(right);
    }

    private FacilityCapitalMarkersOperationResponse operation(
            FacilityOperation operation,
            FacilityCapitalMarkers entity) {

        FacilityCapitalMarkersOperationResponse response =
                new FacilityCapitalMarkersOperationResponse();

        response.setOperation(operation);
        response.setFacilityCapitalMarkers(toResponse(entity));

        return response;
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

    private FacilityCapitalMarkersRequest.CapitalMarkerRequest toRequestMarker(
            FacilityCapitalMarkers.CapitalMarker source) {

        if (source == null) {
            return null;
        }

        FacilityCapitalMarkersRequest.CapitalMarkerRequest target =
                new FacilityCapitalMarkersRequest.CapitalMarkerRequest();

        target.setIndicator(source.getIndicator());
        target.setOverride(source.isOverride());
        target.setOverrideJustification(
                source.getOverrideJustification());

        return target;
    }
}
