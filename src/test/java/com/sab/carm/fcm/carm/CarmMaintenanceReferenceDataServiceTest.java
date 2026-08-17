package com.sab.carm.fcm.carm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.sab.carm.fcm.carm.dto.CarmMaintenanceReferenceData;
import com.sab.carm.fcm.carm.dto.CarmReferenceDataResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CarmMaintenanceReferenceDataServiceTest {

    @Mock
    private CarmReferenceDataService referenceDataService;

    private CarmProperties properties;
    private CarmMaintenanceReferenceDataService service;

    @BeforeEach
    void setUp() {

        properties = new CarmProperties();

        properties.getReferenceData()
                .getDefaults()
                .getFacilityType()
                .setAdvised("Y");

        properties.getReferenceData()
                .getDefaults()
                .getFacilityType()
                .setCommitted("Y");

        properties.getReferenceData()
                .getDefaults()
                .getPurposeCode()
                .setUnconditionalCancellable("Y");

        service =
                new CarmMaintenanceReferenceDataService(
                        referenceDataService,
                        properties);
    }

    @Test
    void shouldBuildFacilityTypeFromActiveCarmRecord() {

        Map<String, Object> row =
                new HashMap<>();

        row.put("FGRPFACGRP", "FAC001");
        row.put("FGRPFGDESC", "Facility One");
        row.put("FGRPARCIND", "N");

        when(referenceDataService.fetchConfiguredReferenceData())
                .thenReturn(
                        referenceData(
                                Collections.singletonList(row),
                                Collections.emptyList()));

        CarmMaintenanceReferenceData result =
                service.refresh();

        assertEquals(
                1,
                result.getFacilityTypes().size());

        assertEquals(
                "FAC001",
                result.getFacilityTypes()
                        .get(0)
                        .getFacilityTypeCode());

        assertEquals(
                "Facility One",
                result.getFacilityTypes()
                        .get(0)
                        .getFacilityTypeDescription());

        assertEquals(
                "Y",
                result.getFacilityTypes()
                        .get(0)
                        .getAdvised());

        assertEquals(
                "Y",
                result.getFacilityTypes()
                        .get(0)
                        .getCommitted());
    }

    @Test
    void shouldIgnoreArchivedFacilityType() {

        Map<String, Object> row =
                new HashMap<>();

        row.put("FGRPFACGRP", "FAC001");
        row.put("FGRPFGDESC", "Facility One");
        row.put("FGRPARCIND", "Y");

        when(referenceDataService.fetchConfiguredReferenceData())
                .thenReturn(
                        referenceData(
                                Collections.singletonList(row),
                                Collections.emptyList()));

        CarmMaintenanceReferenceData result =
                service.refresh();

        assertEquals(
                0,
                result.getFacilityTypes().size());
    }

    @Test
    void shouldBuildPurposeCodeFromActiveCarmRecord() {

        Map<String, Object> row =
                new HashMap<>();

        row.put("FACUADVPUR", "HUB01");
        row.put("FACUFACPUR", "CARM01");
        row.put("FACUFCPDES", "Working Capital");
        row.put("FACUARCIND", "N");

        when(referenceDataService.fetchConfiguredReferenceData())
                .thenReturn(
                        referenceData(
                                Collections.emptyList(),
                                Collections.singletonList(row)));

        CarmMaintenanceReferenceData result =
                service.refresh();

        assertEquals(
                1,
                result.getPurposeCodes().size());

        assertEquals(
                "HUB01",
                result.getPurposeCodes()
                        .get(0)
                        .getPurposeCodeHub());

        assertEquals(
                "CARM01",
                result.getPurposeCodes()
                        .get(0)
                        .getPurposeCodeCarm());

        assertEquals(
                "Working Capital",
                result.getPurposeCodes()
                        .get(0)
                        .getDescription());

        assertEquals(
                "Y",
                result.getPurposeCodes()
                        .get(0)
                        .getUnconditionalCancellable());
    }

    @Test
    void shouldRejectInvalidDefaultIndicator() {

        properties.getReferenceData()
                .getDefaults()
                .getFacilityType()
                .setAdvised("X");

        assertThrows(
                IllegalStateException.class,
                () -> service.refresh());
    }

    private Map<String, List<Map<String, Object>>>
    referenceData(
            List<Map<String, Object>> facilityTypes,
            List<Map<String, Object>> purposeCodes) {

        Map<String, List<Map<String, Object>>> result =
                new HashMap<>();

        result.put("1200", facilityTypes);
        result.put("1060", purposeCodes);

        return result;
    }
}