package com.sab.carm.fcm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sab.carm.fcm.dto.FacilityTypeIndicatorRequest;
import com.sab.carm.fcm.dto.FacilityTypeMaintenanceResponse;
import com.sab.carm.fcm.dto.PurposeCodeIndicatorRequest;
import com.sab.carm.fcm.dto.PurposeCodeMaintenanceResponse;
import com.sab.carm.fcm.entity.FacilityTypeMaintenance;
import com.sab.carm.fcm.entity.MaintenanceHistory;
import com.sab.carm.fcm.entity.PurposeCodeMaintenance;
import com.sab.carm.fcm.repository.FacilityTypeMaintenanceRepository;
import com.sab.carm.fcm.repository.MaintenanceHistoryRepository;
import com.sab.carm.fcm.repository.PurposeCodeMaintenanceRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MaintenanceServiceTest {

    @Mock
    private FacilityTypeMaintenanceRepository facilityTypeRepository;

    @Mock
    private PurposeCodeMaintenanceRepository purposeCodeRepository;

    @Mock
    private MaintenanceHistoryRepository historyRepository;

    private MaintenanceService service;

    @BeforeEach
    void setUp() {

        service =
                new MaintenanceService(
                        facilityTypeRepository,
                        purposeCodeRepository,
                        historyRepository);
    }

    @Test
    void shouldUpdateFacilityTypeIndicators() {

        FacilityTypeMaintenance entity =
                facilityType(
                        "FAC001",
                        "Facility One",
                        "N",
                        "N",
                        true);

        when(facilityTypeRepository
                .findByFacilityTypeCode("FAC001"))
                .thenReturn(Optional.of(entity));

        when(facilityTypeRepository.save(entity))
                .thenReturn(entity);

        FacilityTypeIndicatorRequest request =
                new FacilityTypeIndicatorRequest();

        request.setAdvised("Y");
        request.setCommitted("N");

        FacilityTypeMaintenanceResponse response =
                service.updateFacilityTypeIndicators(
                        "FAC001",
                        request);

        assertEquals(
                "Y",
                response.getAdvised());

        assertEquals(
                "N",
                response.getCommitted());

        verify(facilityTypeRepository)
                .save(entity);

        verify(historyRepository)
                .save(any(MaintenanceHistory.class));
    }

    @Test
    void shouldUpdatePurposeCodeIndicator() {

        PurposeCodeMaintenance entity =
                purposeCode(
                        "HUB01",
                        "CARM01",
                        "Purpose One",
                        "N",
                        true);

        when(purposeCodeRepository
                .findByPurposeCodeHubAndPurposeCodeCarm(
                        "HUB01",
                        "CARM01"))
                .thenReturn(Optional.of(entity));

        when(purposeCodeRepository.save(entity))
                .thenReturn(entity);

        PurposeCodeIndicatorRequest request =
                new PurposeCodeIndicatorRequest();

        request.setUnconditionalCancellable("Y");

        PurposeCodeMaintenanceResponse response =
                service.updatePurposeCodeIndicator(
                        "HUB01",
                        "CARM01",
                        request);

        assertEquals(
                "Y",
                response.getUnconditionalCancellable());

        verify(purposeCodeRepository)
                .save(entity);

        verify(historyRepository)
                .save(any(MaintenanceHistory.class));
    }

    @Test
    void shouldRejectFacilityTypeIndicatorForArchivedRecord() {

        FacilityTypeMaintenance entity =
                facilityType(
                        "FAC001",
                        "Facility One",
                        "N",
                        "N",
                        false);

        when(facilityTypeRepository
                .findByFacilityTypeCode("FAC001"))
                .thenReturn(Optional.of(entity));

        FacilityTypeIndicatorRequest request =
                new FacilityTypeIndicatorRequest();

        request.setAdvised("Y");
        request.setCommitted("Y");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.updateFacilityTypeIndicators(
                        "FAC001",
                        request));
    }

    @Test
    void shouldRejectPurposeCodeIndicatorForArchivedRecord() {

        PurposeCodeMaintenance entity =
                purposeCode(
                        "HUB01",
                        "CARM01",
                        "Purpose One",
                        "N",
                        false);

        when(purposeCodeRepository
                .findByPurposeCodeHubAndPurposeCodeCarm(
                        "HUB01",
                        "CARM01"))
                .thenReturn(Optional.of(entity));

        PurposeCodeIndicatorRequest request =
                new PurposeCodeIndicatorRequest();

        request.setUnconditionalCancellable("Y");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.updatePurposeCodeIndicator(
                        "HUB01",
                        "CARM01",
                        request));
    }

    @Test
    void shouldRejectUnknownFacilityType() {

        when(facilityTypeRepository
                .findByFacilityTypeCode("UNKNOWN"))
                .thenReturn(Optional.empty());

        FacilityTypeIndicatorRequest request =
                new FacilityTypeIndicatorRequest();

        request.setAdvised("Y");
        request.setCommitted("Y");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.updateFacilityTypeIndicators(
                        "UNKNOWN",
                        request));
    }

    @Test
    void shouldRejectUnknownPurposeCode() {

        when(purposeCodeRepository
                .findByPurposeCodeHubAndPurposeCodeCarm(
                        "HUB",
                        "CARM"))
                .thenReturn(Optional.empty());

        PurposeCodeIndicatorRequest request =
                new PurposeCodeIndicatorRequest();

        request.setUnconditionalCancellable("Y");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.updatePurposeCodeIndicator(
                        "HUB",
                        "CARM",
                        request));
    }

    @Test
    void shouldReturnOnlyActiveFacilityTypes() {

        FacilityTypeMaintenance active =
                facilityType(
                        "FAC001",
                        "Facility One",
                        "Y",
                        "N",
                        true);

        FacilityTypeMaintenance archived =
                facilityType(
                        "FAC002",
                        "Facility Two",
                        "Y",
                        "Y",
                        false);

        when(facilityTypeRepository.findAll())
                .thenReturn(
                        java.util.Arrays.asList(
                                active,
                                archived));

        assertEquals(
                1,
                service.getFacilityTypes().size());
    }

    @Test
    void shouldReturnOnlyActivePurposeCodes() {

        PurposeCodeMaintenance active =
                purposeCode(
                        "HUB01",
                        "CARM01",
                        "Purpose One",
                        "Y",
                        true);

        PurposeCodeMaintenance archived =
                purposeCode(
                        "HUB02",
                        "CARM02",
                        "Purpose Two",
                        "Y",
                        false);

        when(purposeCodeRepository.findAll())
                .thenReturn(
                        java.util.Arrays.asList(
                                active,
                                archived));

        assertEquals(
                1,
                service.getPurposeCodes().size());
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