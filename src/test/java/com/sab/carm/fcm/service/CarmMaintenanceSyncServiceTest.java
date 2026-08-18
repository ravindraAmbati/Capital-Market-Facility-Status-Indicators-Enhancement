package com.sab.carm.fcm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sab.carm.fcm.carm.CarmClient;
import com.sab.carm.fcm.carm.CarmProperties;
import com.sab.carm.fcm.carm.dto.CarmReferenceDataResponse;
import com.sab.carm.fcm.entity.FacilityTypeMaintenance;
import com.sab.carm.fcm.entity.MaintenanceHistory;
import com.sab.carm.fcm.entity.PurposeCodeMaintenance;
import com.sab.carm.fcm.repository.FacilityTypeMaintenanceRepository;
import com.sab.carm.fcm.repository.MaintenanceHistoryRepository;
import com.sab.carm.fcm.repository.PurposeCodeMaintenanceRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CarmMaintenanceSyncServiceTest {

    private static final String TABLE_1200 = "1200";
    private static final String TABLE_1060 = "1060";

    @Mock
    private CarmClient carmClient;

    @Mock
    private FacilityTypeMaintenanceRepository facilityTypeRepository;

    @Mock
    private PurposeCodeMaintenanceRepository purposeCodeRepository;

    @Mock
    private MaintenanceHistoryRepository historyRepository;

    private CarmProperties properties;

    private CarmMaintenanceSyncService service;

    @BeforeEach
    void setUp() {
        /*
         * Use the real configuration object.
         *
         * CarmProperties already defines the required defaults:
         *
         * advised = Y
         * committed = Y
         * unconditionalCancellable = Y
         */
        properties = new CarmProperties();

        service = new CarmMaintenanceSyncService(
                carmClient,
                properties,
                facilityTypeRepository,
                purposeCodeRepository,
                historyRepository);
    }

    @Test
    void shouldCreateNewFacilityTypeWithDefaultIndicators() {

        Map<String, Object> row = row(
                "FGRPFACGRP", "FAC001",
                "FGRPFGDESC", "Facility One",
                "FGRPARCIND", "N");

        mockCarmResponse(TABLE_1200, row);

        when(facilityTypeRepository
                .findByFacilityTypeCode("FAC001"))
                .thenReturn(Optional.empty());

        when(facilityTypeRepository.findAll())
                .thenReturn(Collections.emptyList());

        CarmMaintenanceSyncService.SyncResult result =
                service.sync(TABLE_1200);

        assertEquals(
                1,
                result.getSummary().getCreated());

        assertEquals(
                0,
                result.getSummary().getUpdated());

        assertEquals(
                1,
                result.getDetails().size());

        assertEquals(
                "CREATE",
                result.getDetails().get(0).getAction());

        ArgumentCaptor<FacilityTypeMaintenance> captor =
                ArgumentCaptor.forClass(
                        FacilityTypeMaintenance.class);

        verify(facilityTypeRepository)
                .save(captor.capture());

        FacilityTypeMaintenance saved =
                captor.getValue();

        assertEquals(
                "FAC001",
                saved.getFacilityTypeCode());

        assertEquals(
                "Facility One",
                saved.getFacilityTypeDescription());

        /*
         * These values come from CarmProperties defaults.
         */
        assertEquals(
                "Y",
                saved.getAdvised());

        assertEquals(
                "Y",
                saved.getCommitted());

        assertEquals(
                true,
                saved.isActive());
    }

    @Test
    void shouldNotUpdateUnchangedFacilityType() {

        Map<String, Object> row = row(
                "FGRPFACGRP", "FAC001",
                "FGRPFGDESC", "Facility One",
                "FGRPARCIND", "N");

        mockCarmResponse(TABLE_1200, row);

        FacilityTypeMaintenance existing = facilityType(
                "FAC001",
                "Facility One",
                "N",
                "Y",
                true);

        when(facilityTypeRepository
                .findByFacilityTypeCode("FAC001"))
                .thenReturn(Optional.of(existing));

        when(facilityTypeRepository.findAll())
                .thenReturn(
                        Collections.singletonList(existing));

        CarmMaintenanceSyncService.SyncResult result =
                service.sync(TABLE_1200);

        assertEquals(
                0,
                result.getSummary().getCreated());

        assertEquals(
                0,
                result.getSummary().getUpdated());

        assertEquals(
                1,
                result.getSummary().getUnchanged());

        verify(facilityTypeRepository, never())
                .save(any(FacilityTypeMaintenance.class));

        verify(historyRepository, never())
                .save(any(MaintenanceHistory.class));
    }

    @Test
    void shouldUpdateOnlyFacilityTypeDescription() {

        Map<String, Object> row = row(
                "FGRPFACGRP", "FAC001",
                "FGRPFGDESC", "Updated Facility",
                "FGRPARCIND", "N");

        mockCarmResponse(TABLE_1200, row);

        FacilityTypeMaintenance existing = facilityType(
                "FAC001",
                "Old Facility",
                "N",
                "Y",
                true);

        when(facilityTypeRepository
                .findByFacilityTypeCode("FAC001"))
                .thenReturn(Optional.of(existing));

        when(facilityTypeRepository.findAll())
                .thenReturn(
                        Collections.singletonList(existing));

        CarmMaintenanceSyncService.SyncResult result =
                service.sync(TABLE_1200);

        assertEquals(
                1,
                result.getSummary().getUpdated());

        assertEquals(
                "Updated Facility",
                existing.getFacilityTypeDescription());

        /*
         * CARM is not the owner of the application indicators.
         * Refresh must not overwrite them.
         */
        assertEquals(
                "N",
                existing.getAdvised());

        assertEquals(
                "Y",
                existing.getCommitted());

        verify(facilityTypeRepository)
                .save(existing);

        verify(historyRepository)
                .save(any(MaintenanceHistory.class));
    }

    @Test
    void shouldArchiveFacilityTypeMissingFromCarm() {

        mockCarmResponse(
                TABLE_1200,
                row(
                        "FGRPFACGRP", "FAC001",
                        "FGRPFGDESC", "Facility One",
                        "FGRPARCIND", "N"));

        FacilityTypeMaintenance existing = facilityType(
                "FAC002",
                "Facility Two",
                "Y",
                "N",
                true);

        when(facilityTypeRepository
                .findByFacilityTypeCode("FAC001"))
                .thenReturn(Optional.empty());

        when(facilityTypeRepository.findAll())
                .thenReturn(
                        Collections.singletonList(existing));

        CarmMaintenanceSyncService.SyncResult result =
                service.sync(TABLE_1200);

        assertEquals(
                1,
                result.getSummary().getArchived());

        assertEquals(
                false,
                existing.isActive());

        /*
         * Existing indicators remain untouched.
         */
        assertEquals(
                "Y",
                existing.getAdvised());

        assertEquals(
                "N",
                existing.getCommitted());

        verify(facilityTypeRepository)
                .save(existing);

        /*
         * The current service records two history entries
         * for this synchronization.
         */
        verify(historyRepository, times(2))
                .save(any(MaintenanceHistory.class));
    }

    @Test
    void shouldReactivateArchivedFacilityType() {

        mockCarmResponse(
                TABLE_1200,
                row(
                        "FGRPFACGRP", "FAC001",
                        "FGRPFGDESC", "Facility One",
                        "FGRPARCIND", "N"));

        FacilityTypeMaintenance existing = facilityType(
                "FAC001",
                "Old Facility",
                "N",
                "Y",
                false);

        when(facilityTypeRepository
                .findByFacilityTypeCode("FAC001"))
                .thenReturn(Optional.of(existing));

        when(facilityTypeRepository.findAll())
                .thenReturn(
                        Collections.singletonList(existing));

        CarmMaintenanceSyncService.SyncResult result =
                service.sync(TABLE_1200);

        assertEquals(
                1,
                result.getSummary().getReactivated());

        assertEquals(
                true,
                existing.isActive());

        assertEquals(
                "Facility One",
                existing.getFacilityTypeDescription());

        /*
         * Indicators remain unchanged.
         */
        assertEquals(
                "N",
                existing.getAdvised());

        assertEquals(
                "Y",
                existing.getCommitted());

        verify(facilityTypeRepository)
                .save(existing);

        verify(historyRepository)
                .save(any(MaintenanceHistory.class));
    }

    @Test
    void shouldCreatePurposeCodeWithDefaultIndicator() {

        mockCarmResponse(
                TABLE_1060,
                row(
                        "FACUADVPUR", "HUB01",
                        "FACUFACPUR", "CARM01",
                        "FACUFCPDES", "Working Capital",
                        "FACUARCIND", "N"));

        when(purposeCodeRepository
                .findByPurposeCodeHubAndPurposeCodeCarm(
                        "HUB01",
                        "CARM01"))
                .thenReturn(Optional.empty());

        when(purposeCodeRepository.findAll())
                .thenReturn(Collections.emptyList());

        CarmMaintenanceSyncService.SyncResult result =
                service.sync(TABLE_1060);

        assertEquals(
                1,
                result.getSummary().getCreated());

        ArgumentCaptor<PurposeCodeMaintenance> captor =
                ArgumentCaptor.forClass(
                        PurposeCodeMaintenance.class);

        verify(purposeCodeRepository)
                .save(captor.capture());

        PurposeCodeMaintenance saved =
                captor.getValue();

        assertEquals(
                "HUB01",
                saved.getPurposeCodeHub());

        assertEquals(
                "CARM01",
                saved.getPurposeCodeCarm());

        assertEquals(
                "Working Capital",
                saved.getDescription());

        /*
         * Default value from CarmProperties.
         */
        assertEquals(
                "Y",
                saved.getUnconditionalCancellable());

        assertEquals(
                true,
                saved.isActive());
    }

    @Test
    void shouldNotUpdateUnchangedPurposeCode() {

        mockCarmResponse(
                TABLE_1060,
                row(
                        "FACUADVPUR", "HUB01",
                        "FACUFACPUR", "CARM01",
                        "FACUFCPDES", "Working Capital",
                        "FACUARCIND", "N"));

        PurposeCodeMaintenance existing = purposeCode(
                "HUB01",
                "CARM01",
                "Working Capital",
                "N",
                true);

        when(purposeCodeRepository
                .findByPurposeCodeHubAndPurposeCodeCarm(
                        "HUB01",
                        "CARM01"))
                .thenReturn(Optional.of(existing));

        when(purposeCodeRepository.findAll())
                .thenReturn(
                        Collections.singletonList(existing));

        CarmMaintenanceSyncService.SyncResult result =
                service.sync(TABLE_1060);

        assertEquals(
                1,
                result.getSummary().getUnchanged());

        /*
         * CARM refresh must not overwrite the application
         * controlled indicator.
         */
        assertEquals(
                "N",
                existing.getUnconditionalCancellable());

        verify(purposeCodeRepository, never())
                .save(any(PurposeCodeMaintenance.class));

        verify(historyRepository, never())
                .save(any(MaintenanceHistory.class));
    }

    @Test
    void shouldUpdatePurposeCodeDescriptionOnly() {

        mockCarmResponse(
                TABLE_1060,
                row(
                        "FACUADVPUR", "HUB01",
                        "FACUFACPUR", "CARM01",
                        "FACUFCPDES", "Updated Purpose",
                        "FACUARCIND", "N"));

        PurposeCodeMaintenance existing = purposeCode(
                "HUB01",
                "CARM01",
                "Old Purpose",
                "N",
                true);

        when(purposeCodeRepository
                .findByPurposeCodeHubAndPurposeCodeCarm(
                        "HUB01",
                        "CARM01"))
                .thenReturn(Optional.of(existing));

        when(purposeCodeRepository.findAll())
                .thenReturn(
                        Collections.singletonList(existing));

        CarmMaintenanceSyncService.SyncResult result =
                service.sync(TABLE_1060);

        assertEquals(
                1,
                result.getSummary().getUpdated());

        assertEquals(
                "Updated Purpose",
                existing.getDescription());

        /*
         * Indicator must remain untouched.
         */
        assertEquals(
                "N",
                existing.getUnconditionalCancellable());

        verify(purposeCodeRepository)
                .save(existing);

        verify(historyRepository)
                .save(any(MaintenanceHistory.class));
    }

    @Test
    void shouldArchivePurposeCodeMissingFromCarm() {

        mockCarmResponse(
                TABLE_1060,
                row(
                        "FACUADVPUR", "HUB01",
                        "FACUFACPUR", "CARM01",
                        "FACUFCPDES", "Purpose One",
                        "FACUARCIND", "N"));

        PurposeCodeMaintenance existing = purposeCode(
                "HUB02",
                "CARM02",
                "Purpose Two",
                "Y",
                true);

        when(purposeCodeRepository
                .findByPurposeCodeHubAndPurposeCodeCarm(
                        "HUB01",
                        "CARM01"))
                .thenReturn(Optional.empty());

        when(purposeCodeRepository.findAll())
                .thenReturn(
                        Collections.singletonList(existing));

        CarmMaintenanceSyncService.SyncResult result =
                service.sync(TABLE_1060);

        assertEquals(
                1,
                result.getSummary().getArchived());

        assertEquals(
                false,
                existing.isActive());

        verify(purposeCodeRepository)
                .save(existing);

        /*
         * The current service records two history entries
         * for this synchronization.
         */
        verify(historyRepository, times(2))
                .save(any(MaintenanceHistory.class));
    }

    @Test
    void shouldReactivateArchivedPurposeCode() {

        mockCarmResponse(
                TABLE_1060,
                row(
                        "FACUADVPUR", "HUB01",
                        "FACUFACPUR", "CARM01",
                        "FACUFCPDES", "Purpose One",
                        "FACUARCIND", "N"));

        PurposeCodeMaintenance existing = purposeCode(
                "HUB01",
                "CARM01",
                "Old Purpose",
                "N",
                false);

        when(purposeCodeRepository
                .findByPurposeCodeHubAndPurposeCodeCarm(
                        "HUB01",
                        "CARM01"))
                .thenReturn(Optional.of(existing));

        when(purposeCodeRepository.findAll())
                .thenReturn(
                        Collections.singletonList(existing));

        CarmMaintenanceSyncService.SyncResult result =
                service.sync(TABLE_1060);

        assertEquals(
                1,
                result.getSummary().getReactivated());

        assertEquals(
                true,
                existing.isActive());

        assertEquals(
                "Purpose One",
                existing.getDescription());

        /*
         * Indicator remains unchanged.
         */
        assertEquals(
                "N",
                existing.getUnconditionalCancellable());

        verify(purposeCodeRepository)
                .save(existing);

        verify(historyRepository)
                .save(any(MaintenanceHistory.class));
    }

    @Test
    void shouldArchivePurposeCodeWhenCarmMarksItArchived() {

        mockCarmResponse(
                TABLE_1060,
                row(
                        "FACUADVPUR", "HUB01",
                        "FACUFACPUR", "CARM01",
                        "FACUFCPDES", "Purpose One",
                        "FACUARCIND", "Y"));

        PurposeCodeMaintenance existing = purposeCode(
                "HUB01",
                "CARM01",
                "Purpose One",
                "Y",
                true);

        when(purposeCodeRepository
                .findByPurposeCodeHubAndPurposeCodeCarm(
                        "HUB01",
                        "CARM01"))
                .thenReturn(Optional.of(existing));

        when(purposeCodeRepository.findAll())
                .thenReturn(
                        Collections.singletonList(existing));

        CarmMaintenanceSyncService.SyncResult result =
                service.sync(TABLE_1060);

        assertEquals(
                1,
                result.getSummary().getArchived());

        assertEquals(
                false,
                existing.isActive());

        /*
         * Indicator remains unchanged.
         */
        assertEquals(
                "Y",
                existing.getUnconditionalCancellable());

        verify(purposeCodeRepository)
                .save(existing);

        verify(historyRepository)
                .save(any(MaintenanceHistory.class));
    }

    @Test
    void shouldArchiveFacilityTypeWhenCarmMarksItArchived() {

        mockCarmResponse(
                TABLE_1200,
                row(
                        "FGRPFACGRP", "FAC001",
                        "FGRPFGDESC", "Facility One",
                        "FGRPARCIND", "Y"));

        FacilityTypeMaintenance existing = facilityType(
                "FAC001",
                "Facility One",
                "N",
                "Y",
                true);

        when(facilityTypeRepository
                .findByFacilityTypeCode("FAC001"))
                .thenReturn(Optional.of(existing));

        when(facilityTypeRepository.findAll())
                .thenReturn(
                        Collections.singletonList(existing));

        CarmMaintenanceSyncService.SyncResult result =
                service.sync(TABLE_1200);

        assertEquals(
                1,
                result.getSummary().getArchived());

        assertEquals(
                false,
                existing.isActive());

        /*
         * Indicators remain unchanged.
         */
        assertEquals(
                "N",
                existing.getAdvised());

        assertEquals(
                "Y",
                existing.getCommitted());

        verify(facilityTypeRepository)
                .save(existing);

        verify(historyRepository)
                .save(any(MaintenanceHistory.class));
    }

    @Test
    void shouldRejectUnsupportedTable() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.sync("9999"));

        verify(carmClient, never())
                .fetchReferenceData("9999");
    }

    @Test
    void shouldNotArchiveMongoDataWhenCarmFails() {

        when(carmClient.fetchReferenceData(TABLE_1200))
                .thenThrow(
                        new RuntimeException(
                                "CARM unavailable"));

        FacilityTypeMaintenance existing = facilityType(
                "FAC001",
                "Facility One",
                "Y",
                "N",
                true);

        assertThrows(
                RuntimeException.class,
                () -> service.sync(TABLE_1200));

        /*
         * Since CARM is the master, no Mongo maintenance
         * changes are allowed when CARM refresh fails.
         */
        assertEquals(
                true,
                existing.isActive());

        verify(facilityTypeRepository, never())
                .save(any(FacilityTypeMaintenance.class));

        verify(historyRepository, never())
                .save(any(MaintenanceHistory.class));
    }

    @Test
    void shouldMergeAllTables() {

        mockCarmResponse(
                TABLE_1200,
                row(
                        "FGRPFACGRP", "FAC001",
                        "FGRPFGDESC", "Facility One",
                        "FGRPARCIND", "N"));

        mockCarmResponse(
                TABLE_1060,
                row(
                        "FACUADVPUR", "HUB01",
                        "FACUFACPUR", "CARM01",
                        "FACUFCPDES", "Purpose One",
                        "FACUARCIND", "N"));

        when(facilityTypeRepository
                .findByFacilityTypeCode("FAC001"))
                .thenReturn(Optional.empty());

        when(facilityTypeRepository.findAll())
                .thenReturn(Collections.emptyList());

        when(purposeCodeRepository
                .findByPurposeCodeHubAndPurposeCodeCarm(
                        "HUB01",
                        "CARM01"))
                .thenReturn(Optional.empty());

        when(purposeCodeRepository.findAll())
                .thenReturn(Collections.emptyList());

        CarmMaintenanceSyncService.SyncResult result =
                service.syncAll();

        assertEquals(
                "ALL",
                result.getTableName());

        assertEquals(
                2,
                result.getSummary().getCreated());

        assertEquals(
                2,
                result.getDetails().size());
    }

    private void mockCarmResponse(
            String tableName,
            Map<String, Object>... rows) {

        CarmReferenceDataResponse response =
                new CarmReferenceDataResponse();

        CarmReferenceDataResponse.Body body =
                new CarmReferenceDataResponse.Body();

        body.setReferenceTable(
                Arrays.asList(rows));

        response.setBody(body);

        when(carmClient.fetchReferenceData(tableName))
                .thenReturn(response);
    }

    private Map<String, Object> row(
            Object... values) {

        Map<String, Object> row =
                new HashMap<>();

        for (int i = 0; i < values.length; i += 2) {
            row.put(
                    String.valueOf(values[i]),
                    values[i + 1]);
        }

        return row;
    }

    private FacilityTypeMaintenance facilityType(
            String code,
            String description,
            String advised,
            String committed,
            boolean active) {

        FacilityTypeMaintenance entity =
                new FacilityTypeMaintenance();

        entity.setFacilityTypeCode(code);
        entity.setFacilityTypeDescription(description);
        entity.setAdvised(advised);
        entity.setCommitted(committed);
        entity.setActive(active);

        return entity;
    }

    private PurposeCodeMaintenance purposeCode(
            String hub,
            String carm,
            String description,
            String indicator,
            boolean active) {

        PurposeCodeMaintenance entity =
                new PurposeCodeMaintenance();

        entity.setPurposeCodeHub(hub);
        entity.setPurposeCodeCarm(carm);
        entity.setDescription(description);
        entity.setUnconditionalCancellable(indicator);
        entity.setActive(active);

        return entity;
    }
}