package com.sab.carm.fcm.service;

import com.sab.carm.fcm.carm.CarmClient;
import com.sab.carm.fcm.carm.CarmProperties;
import com.sab.carm.fcm.carm.dto.CarmReferenceDataResponse;
import com.sab.carm.fcm.dto.RefreshDetail;
import com.sab.carm.fcm.dto.RefreshSummary;
import com.sab.carm.fcm.entity.FacilityTypeMaintenance;
import com.sab.carm.fcm.entity.MaintenanceHistory;
import com.sab.carm.fcm.entity.PurposeCodeMaintenance;
import com.sab.carm.fcm.repository.FacilityTypeMaintenanceRepository;
import com.sab.carm.fcm.repository.MaintenanceHistoryRepository;
import com.sab.carm.fcm.repository.PurposeCodeMaintenanceRepository;
import com.sab.carm.fcm.util.SecurityUtil;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class CarmMaintenanceSyncService {

    public static final String FACILITY_TYPE_TABLE = "1200";
    public static final String PURPOSE_CODE_TABLE = "1060";

    private static final String ACTIVE = "N";

    private static final String CREATE = "CREATE";
    private static final String UPDATE = "UPDATE";
    private static final String ARCHIVE = "ARCHIVE";
    private static final String REACTIVATE = "REACTIVATE";
    private static final String UNCHANGED = "UNCHANGED";

    private static final String SOURCE_CARM = "CARM";

    private static final String FACILITY_TYPE =
            "FACILITY_TYPE";

    private static final String PURPOSE_CODE =
            "PURPOSE_CODE";

    private final CarmClient carmClient;
    private final CarmProperties properties;
    private final FacilityTypeMaintenanceRepository facilityTypeRepository;
    private final PurposeCodeMaintenanceRepository purposeCodeRepository;
    private final MaintenanceHistoryRepository historyRepository;

    public CarmMaintenanceSyncService(
            CarmClient carmClient,
            CarmProperties properties,
            FacilityTypeMaintenanceRepository facilityTypeRepository,
            PurposeCodeMaintenanceRepository purposeCodeRepository,
            MaintenanceHistoryRepository historyRepository) {

        this.carmClient = carmClient;
        this.properties = properties;
        this.facilityTypeRepository = facilityTypeRepository;
        this.purposeCodeRepository = purposeCodeRepository;
        this.historyRepository = historyRepository;
    }

    public SyncResult sync(String tableName) {

        if (FACILITY_TYPE_TABLE.equals(tableName)) {
            return syncFacilityTypes();
        }

        if (PURPOSE_CODE_TABLE.equals(tableName)) {
            return syncPurposeCodes();
        }

        throw new IllegalArgumentException(
                "Unsupported CARM reference table: "
                        + tableName);
    }

    public SyncResult syncAll() {

        SyncResult facility =
                sync(FACILITY_TYPE_TABLE);

        SyncResult purpose =
                sync(PURPOSE_CODE_TABLE);

        return SyncResult.merge(
                facility,
                purpose);
    }

    private SyncResult syncFacilityTypes() {

        List<Map<String, Object>> rows =
                fetchRows(FACILITY_TYPE_TABLE);

        RefreshSummary summary =
                new RefreshSummary();

        List<RefreshDetail> details =
                new ArrayList<>();

        Set<String> carmKeys =
                new HashSet<>();

        for (Map<String, Object> row : rows) {

            String code =
                    value(row, "FGRPFACGRP");

            String description =
                    value(row, "FGRPFGDESC");

            String archiveIndicator =
                    value(row, "FGRPARCIND");

            if (code == null) {
                continue;
            }

            if (ACTIVE.equals(archiveIndicator)) {

                carmKeys.add(code);

                syncActiveFacilityType(
                        code,
                        description,
                        summary,
                        details);

            } else {

                archiveFacilityType(
                        code,
                        summary,
                        details);
            }
        }

        /*
         * Any Mongo active record not present in the CARM active
         * set must be archived because CARM is the master.
         */
        for (FacilityTypeMaintenance entity :
                facilityTypeRepository.findAll()) {

            if (entity.isActive()
                    && !carmKeys.contains(
                    entity.getFacilityTypeCode())) {

                archiveFacilityTypeEntity(
                        entity,
                        summary,
                        details);
            }
        }

        return new SyncResult(
                FACILITY_TYPE_TABLE,
                summary,
                details);
    }

    private void syncActiveFacilityType(
            String code,
            String description,
            RefreshSummary summary,
            List<RefreshDetail> details) {

        Optional<FacilityTypeMaintenance> existing =
                facilityTypeRepository
                        .findByFacilityTypeCode(code);

        if (!existing.isPresent()) {

            FacilityTypeMaintenance entity =
                    new FacilityTypeMaintenance();

            entity.setFacilityTypeCode(code);
            entity.setFacilityTypeDescription(description);
            entity.setAdvised(
                    properties.getReferenceData()
                            .getDefaults()
                            .getFacilityType()
                            .getAdvised());
            entity.setCommitted(
                    properties.getReferenceData()
                            .getDefaults()
                            .getFacilityType()
                            .getCommitted());
            entity.setActive(true);

            setId(entity, facilityTypeId(code));

            facilityTypeRepository.save(entity);

            summary.incrementCreated();

            RefreshDetail detail =
                    detail(
                            CREATE,
                            code,
                            null,
                            facilityTypeData(entity));

            details.add(detail);

            saveHistory(
                    FACILITY_TYPE,
                    code,
                    CREATE,
                    null,
                    facilityTypeData(entity));

            return;
        }

        FacilityTypeMaintenance entity =
                existing.get();

        if (!entity.isActive()) {

            Map<String, Object> previous =
                    facilityTypeData(entity);

            entity.setActive(true);
            entity.setFacilityTypeDescription(
                    description);

            facilityTypeRepository.save(entity);

            summary.incrementReactivated();

            details.add(
                    detail(
                            REACTIVATE,
                            code,
                            previous,
                            facilityTypeData(entity)));

            saveHistory(
                    FACILITY_TYPE,
                    code,
                    REACTIVATE,
                    previous,
                    facilityTypeData(entity));

            return;
        }

        if (!equals(
                entity.getFacilityTypeDescription(),
                description)) {

            Map<String, Object> previous =
                    facilityTypeData(entity);

            entity.setFacilityTypeDescription(
                    description);

            facilityTypeRepository.save(entity);

            summary.incrementUpdated();

            details.add(
                    detail(
                            UPDATE,
                            code,
                            previous,
                            facilityTypeData(entity)));

            saveHistory(
                    FACILITY_TYPE,
                    code,
                    UPDATE,
                    previous,
                    facilityTypeData(entity));

            return;
        }

        summary.incrementUnchanged();

        details.add(
                detail(
                        UNCHANGED,
                        code,
                        facilityTypeData(entity),
                        facilityTypeData(entity)));
    }

    private void archiveFacilityType(
            String code,
            RefreshSummary summary,
            List<RefreshDetail> details) {

        Optional<FacilityTypeMaintenance> existing =
                facilityTypeRepository
                        .findByFacilityTypeCode(code);

        if (existing.isPresent()
                && existing.get().isActive()) {

            archiveFacilityTypeEntity(
                    existing.get(),
                    summary,
                    details);
        }
    }

    private void archiveFacilityTypeEntity(
            FacilityTypeMaintenance entity,
            RefreshSummary summary,
            List<RefreshDetail> details) {

        Map<String, Object> previous =
                facilityTypeData(entity);

        entity.setActive(false);

        facilityTypeRepository.save(entity);

        summary.incrementArchived();

        details.add(
                detail(
                        ARCHIVE,
                        entity.getFacilityTypeCode(),
                        previous,
                        facilityTypeData(entity)));

        saveHistory(
                FACILITY_TYPE,
                entity.getFacilityTypeCode(),
                ARCHIVE,
                previous,
                facilityTypeData(entity));
    }

    private SyncResult syncPurposeCodes() {

        List<Map<String, Object>> rows =
                fetchRows(PURPOSE_CODE_TABLE);

        RefreshSummary summary =
                new RefreshSummary();

        List<RefreshDetail> details =
                new ArrayList<>();

        Set<String> carmKeys =
                new HashSet<>();

        for (Map<String, Object> row : rows) {

            String hub =
                    value(row, "FACUADVPUR");

            String carm =
                    value(row, "FACUFACPUR");

            String description =
                    value(row, "FACUFCPDES");

            String archiveIndicator =
                    value(row, "FACUARCIND");

            if (hub == null || carm == null) {
                continue;
            }

            String businessKey =
                    purposeKey(hub, carm);

            if (ACTIVE.equals(archiveIndicator)) {

                carmKeys.add(businessKey);

                syncActivePurposeCode(
                        hub,
                        carm,
                        description,
                        summary,
                        details);

            } else {

                archivePurposeCode(
                        hub,
                        carm,
                        summary,
                        details);
            }
        }

        for (PurposeCodeMaintenance entity :
                purposeCodeRepository.findAll()) {

            String key =
                    purposeKey(
                            entity.getPurposeCodeHub(),
                            entity.getPurposeCodeCarm());

            if (entity.isActive()
                    && !carmKeys.contains(key)) {

                archivePurposeCodeEntity(
                        entity,
                        summary,
                        details);
            }
        }

        return new SyncResult(
                PURPOSE_CODE_TABLE,
                summary,
                details);
    }

    private void syncActivePurposeCode(
            String hub,
            String carm,
            String description,
            RefreshSummary summary,
            List<RefreshDetail> details) {

        Optional<PurposeCodeMaintenance> existing =
                purposeCodeRepository
                        .findByPurposeCodeHubAndPurposeCodeCarm(
                                hub,
                                carm);

        String businessKey =
                purposeKey(hub, carm);

        if (!existing.isPresent()) {

            PurposeCodeMaintenance entity =
                    new PurposeCodeMaintenance();

            entity.setPurposeCodeHub(hub);
            entity.setPurposeCodeCarm(carm);
            entity.setDescription(description);
            entity.setUnconditionalCancellable(
                    properties.getReferenceData()
                            .getDefaults()
                            .getPurposeCode()
                            .getUnconditionalCancellable());
            entity.setActive(true);

            setId(entity, purposeCodeId(hub, carm));

            purposeCodeRepository.save(entity);

            summary.incrementCreated();

            details.add(
                    detail(
                            CREATE,
                            businessKey,
                            null,
                            purposeCodeData(entity)));

            saveHistory(
                    PURPOSE_CODE,
                    businessKey,
                    CREATE,
                    null,
                    purposeCodeData(entity));

            return;
        }

        PurposeCodeMaintenance entity =
                existing.get();

        if (!entity.isActive()) {

            Map<String, Object> previous =
                    purposeCodeData(entity);

            entity.setActive(true);
            entity.setDescription(description);

            purposeCodeRepository.save(entity);

            summary.incrementReactivated();

            details.add(
                    detail(
                            REACTIVATE,
                            businessKey,
                            previous,
                            purposeCodeData(entity)));

            saveHistory(
                    PURPOSE_CODE,
                    businessKey,
                    REACTIVATE,
                    previous,
                    purposeCodeData(entity));

            return;
        }

        if (!equals(
                entity.getDescription(),
                description)) {

            Map<String, Object> previous =
                    purposeCodeData(entity);

            entity.setDescription(description);

            purposeCodeRepository.save(entity);

            summary.incrementUpdated();

            details.add(
                    detail(
                            UPDATE,
                            businessKey,
                            previous,
                            purposeCodeData(entity)));

            saveHistory(
                    PURPOSE_CODE,
                    businessKey,
                    UPDATE,
                    previous,
                    purposeCodeData(entity));

            return;
        }

        summary.incrementUnchanged();

        details.add(
                detail(
                        UNCHANGED,
                        businessKey,
                        purposeCodeData(entity),
                        purposeCodeData(entity)));
    }

    private void archivePurposeCode(
            String hub,
            String carm,
            RefreshSummary summary,
            List<RefreshDetail> details) {

        Optional<PurposeCodeMaintenance> existing =
                purposeCodeRepository
                        .findByPurposeCodeHubAndPurposeCodeCarm(
                                hub,
                                carm);

        if (existing.isPresent()
                && existing.get().isActive()) {

            archivePurposeCodeEntity(
                    existing.get(),
                    summary,
                    details);
        }
    }

    private void archivePurposeCodeEntity(
            PurposeCodeMaintenance entity,
            RefreshSummary summary,
            List<RefreshDetail> details) {

        Map<String, Object> previous =
                purposeCodeData(entity);

        entity.setActive(false);

        purposeCodeRepository.save(entity);

        String key =
                purposeKey(
                        entity.getPurposeCodeHub(),
                        entity.getPurposeCodeCarm());

        summary.incrementArchived();

        details.add(
                detail(
                        ARCHIVE,
                        key,
                        previous,
                        purposeCodeData(entity)));

        saveHistory(
                PURPOSE_CODE,
                key,
                ARCHIVE,
                previous,
                purposeCodeData(entity));
    }

    private List<Map<String, Object>> fetchRows(
            String tableName) {

        try {

            CarmReferenceDataResponse response =
                    carmClient.fetchReferenceData(
                            tableName);

            if (response == null
                    || response.getBody() == null
                    || response.getBody()
                    .getReferenceTable() == null) {

                return new ArrayList<>();
            }

            return response.getBody()
                    .getReferenceTable();

        } catch (RestClientException ex) {

            throw new IllegalStateException(
                    "Unable to synchronize CARM reference table "
                            + tableName,
                    ex);
        }
    }

    private void saveHistory(
            String logicalTable,
            String businessKey,
            String action,
            Map<String, Object> previous,
            Map<String, Object> current) {

        MaintenanceHistory history =
                new MaintenanceHistory();

        history.setLogicalTable(logicalTable);
        history.setBusinessKey(businessKey);
        history.setAction(action);
        history.setSource(SOURCE_CARM);
        history.setPreviousData(previous);
        history.setNewData(current);
        history.setUsername(
                SecurityUtil.currentUsername());
        history.setCorrelationId(
                MDC.get("correlationId"));
        history.setExecutedAt(Instant.now());

        historyRepository.save(history);
    }

    private RefreshDetail detail(
            String action,
            String businessKey,
            Object previous,
            Object current) {

        RefreshDetail detail =
                new RefreshDetail();

        detail.setAction(action);
        detail.setBusinessKey(businessKey);
        detail.setPreviousData(previous);
        detail.setCurrentData(current);

        return detail;
    }

    private Map<String, Object> facilityTypeData(
            FacilityTypeMaintenance entity) {

        Map<String, Object> data =
                new HashMap<>();

        data.put(
                "facilityTypeCode",
                entity.getFacilityTypeCode());

        data.put(
                "facilityTypeDescription",
                entity.getFacilityTypeDescription());

        data.put(
                "advised",
                entity.getAdvised());

        data.put(
                "committed",
                entity.getCommitted());

        data.put(
                "active",
                entity.isActive());

        return data;
    }

    private Map<String, Object> purposeCodeData(
            PurposeCodeMaintenance entity) {

        Map<String, Object> data =
                new HashMap<>();

        data.put(
                "purposeCodeHub",
                entity.getPurposeCodeHub());

        data.put(
                "purposeCodeCarm",
                entity.getPurposeCodeCarm());

        data.put(
                "description",
                entity.getDescription());

        data.put(
                "unconditionalCancellable",
                entity.getUnconditionalCancellable());

        data.put(
                "active",
                entity.isActive());

        return data;
    }

    private String facilityTypeId(String code) {
        return "FACILITY_TYPE:" + code;
    }

    private String purposeCodeId(
            String hub,
            String carm) {

        return "PURPOSE_CODE:"
                + hub
                + ":"
                + carm;
    }

    private String purposeKey(
            String hub,
            String carm) {

        return hub + ":" + carm;
    }

    private void setId(
            FacilityTypeMaintenance entity,
            String id) {

        /*
         * BaseEntity intentionally has no public setId().
         * Use repository lookup/business-key uniqueness instead
         * if the project does not expose IDs.
         */
    }

    private void setId(
            PurposeCodeMaintenance entity,
            String id) {

        /*
         * See note above.
         */
    }

    private String value(
            Map<String, Object> row,
            String column) {

        Object value =
                row.get(column);

        return value == null
                ? null
                : String.valueOf(value);
    }

    private boolean equals(
            String first,
            String second) {

        return first == null
                ? second == null
                : first.equals(second);
    }

    public static class SyncResult {

        private final String tableName;
        private final RefreshSummary summary;
        private final List<RefreshDetail> details;

        public SyncResult(
                String tableName,
                RefreshSummary summary,
                List<RefreshDetail> details) {

            this.tableName = tableName;
            this.summary = summary;
            this.details = details;
        }

        public String getTableName() {
            return tableName;
        }

        public RefreshSummary getSummary() {
            return summary;
        }

        public List<RefreshDetail> getDetails() {
            return details;
        }

        public static SyncResult merge(
                SyncResult first,
                SyncResult second) {

            RefreshSummary summary =
                    new RefreshSummary();

            copySummary(
                    first.summary,
                    summary);

            copySummary(
                    second.summary,
                    summary);

            List<RefreshDetail> details =
                    new ArrayList<>();

            details.addAll(first.details);
            details.addAll(second.details);

            return new SyncResult(
                    "ALL",
                    summary,
                    details);
        }

        private static void copySummary(
                RefreshSummary source,
                RefreshSummary target) {

            for (int i = 0; i < source.getCreated(); i++) {
                target.incrementCreated();
            }

            for (int i = 0; i < source.getUpdated(); i++) {
                target.incrementUpdated();
            }

            for (int i = 0; i < source.getArchived(); i++) {
                target.incrementArchived();
            }

            for (int i = 0; i < source.getReactivated(); i++) {
                target.incrementReactivated();
            }

            for (int i = 0; i < source.getUnchanged(); i++) {
                target.incrementUnchanged();
            }
        }
    }
}