package com.sab.carm.fcm.ui.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sab.carm.fcm.dto.FacilityTypeMaintenanceResponse;
import com.sab.carm.fcm.dto.PurposeCodeMaintenanceResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UiTestDataStoreTest {

    @Test
    void shouldLoadStandardFixture() throws Exception {

        UiTestDataStore store =
                new UiTestDataStore(
                        new ObjectMapper(),
                        new DefaultResourceLoader());

        store.reset(
                "classpath:/mongodb/test-standard.json");

        List<FacilityTypeMaintenanceResponse> facilityTypes =
                store.getFacilityTypes();

        List<PurposeCodeMaintenanceResponse> purposeCodes =
                store.getPurposeCodes();

        assertEquals(3, facilityTypes.size());
        assertEquals(3, purposeCodes.size());

        assertEquals(
                "FT001",
                facilityTypes.get(0).getFacilityTypeCode());

        assertEquals(
                "PC01",
                purposeCodes.get(0).getPurposeCodeCarm());
    }

    @Test
    void shouldSupportCreateUpdateAndDeleteInMemory()
            throws Exception {

        UiTestDataStore store =
                new UiTestDataStore(
                        new ObjectMapper(),
                        new DefaultResourceLoader());

        store.reset(
                "classpath:/mongodb/test-standard.json");

        store.saveFacilityType(
                "FT999",
                "Test Facility",
                "Y",
                "N");

        assertTrue(store.facilityTypeExists("FT999"));
        assertEquals(
                4,
                store.getFacilityTypes().size());

        store.saveFacilityType(
                "FT999",
                "Updated Facility",
                "N",
                "Y");

        assertEquals(
                "Updated Facility",
                store.getFacilityTypes()
                        .stream()
                        .filter(item ->
                                "FT999".equals(
                                        item.getFacilityTypeCode()))
                        .findFirst()
                        .get()
                        .getFacilityTypeDescription());

        store.deleteFacilityType("FT999");

        assertEquals(
                3,
                store.getFacilityTypes().size());
    }
}
