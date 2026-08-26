package com.sab.carm.fcm.service;

import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersDefaultsResponse;
import com.sab.carm.fcm.entity.FacilityTypeMaintenance;
import com.sab.carm.fcm.entity.PurposeCodeMaintenance;
import com.sab.carm.fcm.repository.FacilityTypeMaintenanceRepository;
import com.sab.carm.fcm.repository.PurposeCodeMaintenanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FacilityCapitalMarkersDefaultsServiceTest {

    @Mock
    private FacilityTypeMaintenanceRepository facilityTypeRepository;

    @Mock
    private PurposeCodeMaintenanceRepository purposeCodeRepository;

    private FacilityCapitalMarkersDefaultsService service;

    @BeforeEach
    void setUp() {
        service = new FacilityCapitalMarkersDefaultsService(
                facilityTypeRepository,
                purposeCodeRepository);
    }

    @Test
    void shouldReturnAllFacilityTypeAndPurposeCodeDefaults() {
        FacilityTypeMaintenance facilityType =
                new FacilityTypeMaintenance();
        facilityType.setFacilityTypeCode("FT01");
        facilityType.setFacilityTypeDescription("Facility Type 1");
        facilityType.setAdvised("Y");
        facilityType.setCommitted("N");

        PurposeCodeMaintenance purpose =
                new PurposeCodeMaintenance();
        purpose.setPurposeCodeCarm("CARM01");
        purpose.setPurposeCodeHub("HUB01");
        purpose.setPurposeCodeDescription("Purpose 1");
        purpose.setUnconditionalCancellable("Y");

        when(facilityTypeRepository.findAll())
                .thenReturn(Collections.singletonList(facilityType));
        when(purposeCodeRepository.findAll())
                .thenReturn(Collections.singletonList(purpose));

        FacilityCapitalMarkersDefaultsResponse result =
                service.findAll();

        assertEquals(1, result.getFacilityTypes().size());
        assertEquals("FT01",
                result.getFacilityTypes().get(0).getFacilityTypeCode());
        assertEquals("Y",
                result.getFacilityTypes().get(0).getAdvised());
        assertEquals("N",
                result.getFacilityTypes().get(0).getCommitted());

        assertEquals(1, result.getPurposeCodes().size());
        assertEquals("CARM01",
                result.getPurposeCodes().get(0).getPurposeCodeCarm());
        assertEquals("HUB01",
                result.getPurposeCodes().get(0).getPurposeCodeHub());
        assertEquals("Y",
                result.getPurposeCodes().get(0)
                        .getUnconditionalCancellable());
    }

    @Test
    void shouldReturnEmptyListsWhenMaintenanceTablesAreEmpty() {
        when(facilityTypeRepository.findAll())
                .thenReturn(Collections.emptyList());
        when(purposeCodeRepository.findAll())
                .thenReturn(Collections.emptyList());

        FacilityCapitalMarkersDefaultsResponse result =
                service.findAll();

        assertEquals(0, result.getFacilityTypes().size());
        assertEquals(0, result.getPurposeCodes().size());
    }
}
