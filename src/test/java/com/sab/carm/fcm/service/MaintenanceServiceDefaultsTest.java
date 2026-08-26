package com.sab.carm.fcm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.sab.carm.fcm.dto.integration.DefaultsResponse;
import com.sab.carm.fcm.entity.FacilityTypeMaintenance;
import com.sab.carm.fcm.entity.PurposeCodeMaintenance;
import com.sab.carm.fcm.repository.FacilityTypeMaintenanceRepository;
import com.sab.carm.fcm.repository.MaintenanceHistoryRepository;
import com.sab.carm.fcm.repository.PurposeCodeMaintenanceRepository;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class MaintenanceServiceDefaultsTest {

    @Mock
    private FacilityTypeMaintenanceRepository facilityTypes;

    @Mock
    private PurposeCodeMaintenanceRepository purposeCodes;

    @Mock
    private MaintenanceHistoryRepository historyRepository;

    private MaintenanceService service;

    @BeforeEach
    void setUp() {
        service = new MaintenanceService(
                facilityTypes,
                purposeCodes,
                historyRepository);
    }

    @Test
    void shouldReturnAllActiveMaintenanceData() {
        FacilityTypeMaintenance facilityType =
                new FacilityTypeMaintenance();
        facilityType.setFacilityTypeCode("FT01");
        facilityType.setFacilityTypeDescription("Facility One");
        facilityType.setAdvised("Y");
        facilityType.setCommitted("N");
        facilityType.setActive(true);

        FacilityTypeMaintenance archivedFacilityType =
                new FacilityTypeMaintenance();
        archivedFacilityType.setFacilityTypeCode("FT02");
        archivedFacilityType.setFacilityTypeDescription("Archived");
        archivedFacilityType.setActive(false);

        PurposeCodeMaintenance purposeCode =
                new PurposeCodeMaintenance();
        purposeCode.setPurposeCodeHub("HUB01");
        purposeCode.setPurposeCodeCarm("CARM01");
        purposeCode.setDescription("Purpose One");
        purposeCode.setUnconditionalCancellable("Y");
        purposeCode.setActive(true);

        PurposeCodeMaintenance archivedPurposeCode =
                new PurposeCodeMaintenance();
        archivedPurposeCode.setPurposeCodeHub("HUB02");
        archivedPurposeCode.setPurposeCodeCarm("CARM02");
        archivedPurposeCode.setDescription("Archived");
        archivedPurposeCode.setActive(false);

        when(facilityTypes.findAll())
                .thenReturn(Arrays.asList(
                        facilityType,
                        archivedFacilityType));

        when(purposeCodes.findAll())
                .thenReturn(Arrays.asList(
                        purposeCode,
                        archivedPurposeCode));

        DefaultsResponse response = service.getDefaults();

        assertEquals(1, response.getFacilityTypes().size());
        assertEquals(
                "FT01",
                response.getFacilityTypes()
                        .get(0)
                        .getFacilityTypeCode());
        assertEquals(
                "Y",
                response.getFacilityTypes()
                        .get(0)
                        .getAdvised());
        assertEquals(
                "N",
                response.getFacilityTypes()
                        .get(0)
                        .getCommitted());

        assertEquals(1, response.getPurposeCodes().size());
        assertEquals(
                "HUB01",
                response.getPurposeCodes()
                        .get(0)
                        .getPurposeCodeHub());
        assertEquals(
                "CARM01",
                response.getPurposeCodes()
                        .get(0)
                        .getPurposeCodeCarm());
        assertEquals(
                "Y",
                response.getPurposeCodes()
                        .get(0)
                        .getUnconditionalCancellable());
    }

    @Test
    void shouldReturnEmptyListsWhenNoActiveMaintenanceDataExists() {
        when(facilityTypes.findAll())
                .thenReturn(Collections.emptyList());
        when(purposeCodes.findAll())
                .thenReturn(Collections.emptyList());

        DefaultsResponse response = service.getDefaults();

        assertEquals(0, response.getFacilityTypes().size());
        assertEquals(0, response.getPurposeCodes().size());
    }
}
