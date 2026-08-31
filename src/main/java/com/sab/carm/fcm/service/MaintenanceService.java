package com.sab.carm.fcm.service;

import com.sab.carm.fcm.dto.FacilityTypeIndicatorRequest;
import com.sab.carm.fcm.dto.FacilityTypeMaintenanceResponse;
import com.sab.carm.fcm.dto.PurposeCodeIndicatorRequest;
import com.sab.carm.fcm.dto.PurposeCodeMaintenanceResponse;
import com.sab.carm.fcm.dto.integration.DefaultsResponse;
import com.sab.carm.fcm.entity.FacilityTypeMaintenance;
import com.sab.carm.fcm.entity.MaintenanceHistory;
import com.sab.carm.fcm.entity.PurposeCodeMaintenance;
import com.sab.carm.fcm.repository.FacilityTypeMaintenanceRepository;
import com.sab.carm.fcm.repository.MaintenanceHistoryRepository;
import com.sab.carm.fcm.repository.PurposeCodeMaintenanceRepository;
import com.sab.carm.fcm.util.SecurityUtil;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MaintenanceService {
    private static final String FACILITY_TYPE = "FACILITY_TYPE";
    private static final String PURPOSE_CODE = "PURPOSE_CODE";
    private static final String USER = "USER";
    private static final String INDICATOR_UPDATE = "INDICATOR_UPDATE";

    private final FacilityTypeMaintenanceRepository facilityTypes;
    private final PurposeCodeMaintenanceRepository purposeCodes;
    private final MaintenanceHistoryRepository historyRepository;

    public MaintenanceService(FacilityTypeMaintenanceRepository facilityTypes,
                              PurposeCodeMaintenanceRepository purposeCodes,
                              MaintenanceHistoryRepository historyRepository) {
        this.facilityTypes = facilityTypes;
        this.purposeCodes = purposeCodes;
        this.historyRepository = historyRepository;
    }

    public List<FacilityTypeMaintenanceResponse> getFacilityTypes() {
        return facilityTypes.findAll().stream()
                .filter(FacilityTypeMaintenance::isActive)
                .map(this::toFacilityTypeResponse)
                .collect(Collectors.toList());
    }

    public FacilityTypeMaintenanceResponse getFacilityType(String code) {
        FacilityTypeMaintenance entity = facilityTypes.findByFacilityTypeCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Facility type not found: " + code));
        return toFacilityTypeResponse(entity);
    }

    public FacilityTypeMaintenanceResponse updateFacilityTypeIndicators(
            String code, FacilityTypeIndicatorRequest request) {
        validateIndicator(request.getAdvised());
        validateIndicator(request.getCommitted());

        FacilityTypeMaintenance entity = facilityTypes.findByFacilityTypeCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Facility type not found: " + code));
        if (!entity.isActive()) {
            throw new IllegalArgumentException("Cannot update archived facility type: " + code);
        }

        Map<String,Object> previous = new HashMap<String,Object>();
        previous.put("advised", entity.getAdvised());
        previous.put("committed", entity.getCommitted());

        entity.setAdvised(request.getAdvised());
        entity.setCommitted(request.getCommitted());
        FacilityTypeMaintenance saved = facilityTypes.save(entity);

        Map<String,Object> current = new HashMap<String,Object>();
        current.put("advised", saved.getAdvised());
        current.put("committed", saved.getCommitted());

        saveHistory(FACILITY_TYPE, code, previous, current);
        return toFacilityTypeResponse(saved);
    }

    public List<PurposeCodeMaintenanceResponse> getPurposeCodes() {
        return purposeCodes.findAll().stream()
                .filter(PurposeCodeMaintenance::isActive)
                .map(this::toPurposeCodeResponse)
                .collect(Collectors.toList());
    }

    public PurposeCodeMaintenanceResponse getPurposeCode(String hub, String carm) {
        PurposeCodeMaintenance entity = purposeCodes
                .findByPurposeCodeHubAndPurposeCodeCarm(hub, carm)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Purpose code not found: " + hub + ":" + carm));
        return toPurposeCodeResponse(entity);
    }

    public PurposeCodeMaintenanceResponse updatePurposeCodeIndicator(
            String hub, String carm, PurposeCodeIndicatorRequest request) {
        validateIndicator(request.getUnconditionalCancellable());

        PurposeCodeMaintenance entity = purposeCodes
                .findByPurposeCodeHubAndPurposeCodeCarm(hub, carm)
                .orElseThrow(() -> new IllegalArgumentException("Purpose code not found"));
        if (!entity.isActive()) {
            throw new IllegalArgumentException("Cannot update archived purpose code");
        }

        Map<String,Object> previous = new HashMap<String,Object>();
        previous.put("unconditionalCancellable",
                entity.getUnconditionalCancellable());

        entity.setUnconditionalCancellable(
                request.getUnconditionalCancellable());
        PurposeCodeMaintenance saved = purposeCodes.save(entity);

        Map<String,Object> current = new HashMap<String,Object>();
        current.put("unconditionalCancellable",
                saved.getUnconditionalCancellable());

        saveHistory(PURPOSE_CODE, hub + ":" + carm, previous, current);
        return toPurposeCodeResponse(saved);
    }

    public FacilityTypeMaintenanceResponse saveFacilityType(
            String code, String description, String advised,
            String committed, boolean isNew) {

        validateRequired(code, "Facility type code");
        validateRequired(description, "Facility type description");
        validateIndicator(advised);
        validateIndicator(committed);

        if (isNew && facilityTypes.findByFacilityTypeCode(code).isPresent()) {
            throw new IllegalArgumentException(
                    "Facility type already exists: " + code);
        }

        FacilityTypeMaintenance entity;
        if (isNew) {
            entity = new FacilityTypeMaintenance();
            entity.setFacilityTypeCode(code);
            entity.setActive(true);
        } else {
            entity = facilityTypes.findByFacilityTypeCode(code)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Facility type not found: " + code));
        }

        entity.setFacilityTypeDescription(description);
        entity.setAdvised(advised);
        entity.setCommitted(committed);
        return toFacilityTypeResponse(facilityTypes.save(entity));
    }

    public void deleteFacilityType(String code) {
        validateRequired(code, "Facility type code");
        FacilityTypeMaintenance entity = facilityTypes.findByFacilityTypeCode(code)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Facility type not found: " + code));
        entity.setActive(false);
        facilityTypes.save(entity);
    }

    public PurposeCodeMaintenanceResponse savePurposeCode(
            String hub, String carm, String description,
            String unconditionalCancellable, boolean isNew) {

        validateRequired(hub, "HUB purpose code");
        validateRequired(carm, "CARM purpose code");
        validateRequired(description, "Purpose code description");
        validateIndicator(unconditionalCancellable);

        if (isNew && purposeCodes
                .findByPurposeCodeHubAndPurposeCodeCarm(hub, carm)
                .isPresent()) {
            throw new IllegalArgumentException("Purpose code already exists.");
        }

        PurposeCodeMaintenance entity;
        if (isNew) {
            entity = new PurposeCodeMaintenance();
            entity.setPurposeCodeHub(hub);
            entity.setPurposeCodeCarm(carm);
            entity.setActive(true);
        } else {
            entity = purposeCodes
                    .findByPurposeCodeHubAndPurposeCodeCarm(hub, carm)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Purpose code not found"));
        }

        entity.setDescription(description);
        entity.setUnconditionalCancellable(unconditionalCancellable);
        return toPurposeCodeResponse(purposeCodes.save(entity));
    }

    public void deletePurposeCode(String hub, String carm) {
        validateRequired(hub, "HUB purpose code");
        validateRequired(carm, "CARM purpose code");

        PurposeCodeMaintenance entity = purposeCodes
                .findByPurposeCodeHubAndPurposeCodeCarm(hub, carm)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Purpose code not found"));

        entity.setActive(false);
        purposeCodes.save(entity);
    }

    public DefaultsResponse getDefaults() {
        DefaultsResponse response = new DefaultsResponse();
        response.setFacilityTypes(getFacilityTypes());
        response.setPurposeCodes(getPurposeCodes());
        return response;
    }

    private void validateRequired(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " is required.");
        }
    }

    private void validateIndicator(String value) {
        if (!"Y".equals(value) && !"N".equals(value)) {
            throw new IllegalArgumentException("Indicator must be Y or N.");
        }
    }

    private void saveHistory(String logicalTable, String businessKey,
                             Map<String,Object> previous,
                             Map<String,Object> current) {
        MaintenanceHistory history = new MaintenanceHistory();
        history.setLogicalTable(logicalTable);
        history.setBusinessKey(businessKey);
        history.setAction(INDICATOR_UPDATE);
        history.setSource(USER);
        history.setPreviousData(previous);
        history.setNewData(current);
        history.setUsername(SecurityUtil.currentUsername());
        history.setCorrelationId(MDC.get("correlationId"));
        history.setExecutedAt(Instant.now());
        historyRepository.save(history);
    }

    private FacilityTypeMaintenanceResponse toFacilityTypeResponse(
            FacilityTypeMaintenance entity) {
        FacilityTypeMaintenanceResponse response =
                new FacilityTypeMaintenanceResponse();
        response.setFacilityTypeCode(entity.getFacilityTypeCode());
        response.setFacilityTypeDescription(entity.getFacilityTypeDescription());
        response.setAdvised(entity.getAdvised());
        response.setCommitted(entity.getCommitted());
        response.setActive(entity.isActive());
        return response;
    }

    private PurposeCodeMaintenanceResponse toPurposeCodeResponse(
            PurposeCodeMaintenance entity) {
        PurposeCodeMaintenanceResponse response =
                new PurposeCodeMaintenanceResponse();
        response.setPurposeCodeHub(entity.getPurposeCodeHub());
        response.setPurposeCodeCarm(entity.getPurposeCodeCarm());
        response.setDescription(entity.getDescription());
        response.setUnconditionalCancellable(
                entity.getUnconditionalCancellable());
        response.setActive(entity.isActive());
        return response;
    }
}
