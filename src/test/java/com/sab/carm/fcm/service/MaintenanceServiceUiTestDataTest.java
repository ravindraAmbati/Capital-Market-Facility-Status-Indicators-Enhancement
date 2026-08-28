package com.sab.carm.fcm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sab.carm.fcm.dto.FacilityTypeIndicatorRequest;
import com.sab.carm.fcm.repository.FacilityTypeMaintenanceRepository;
import com.sab.carm.fcm.repository.MaintenanceHistoryRepository;
import com.sab.carm.fcm.repository.PurposeCodeMaintenanceRepository;
import com.sab.carm.fcm.ui.testdata.UiTestDataProperties;
import com.sab.carm.fcm.ui.testdata.UiTestDataStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class MaintenanceServiceUiTestDataTest {

    private MaintenanceService service;

    @BeforeEach
    void setUp() throws Exception {

        UiTestDataProperties properties =
                new UiTestDataProperties();

        properties.setEnabled(true);
        properties.setScenario("standard");
        properties.setDirectory(
                "classpath:/mongodb/");

        UiTestDataStore store =
                new UiTestDataStore(
                        new ObjectMapper(),
                        new DefaultResourceLoader());

        store.reset(
                "classpath:/mongodb/test-standard.json");

        service = new MaintenanceService(
                mock(
                        FacilityTypeMaintenanceRepository.class),
                mock(
                        PurposeCodeMaintenanceRepository.class),
                mock(
                        MaintenanceHistoryRepository.class),
                properties,
                store);
    }

    @Test
    void shouldReadFacilityTypesFromJson() {

        assertEquals(
                3,
                service.getFacilityTypes().size());

        assertEquals(
                "FT001",
                service.getFacilityTypes()
                        .get(0)
                        .getFacilityTypeCode());
    }

    @Test
    void shouldReadPurposeCodesFromJson() {

        assertEquals(
                3,
                service.getPurposeCodes().size());

        assertEquals(
                "PC01",
                service.getPurposeCodes()
                        .get(0)
                        .getPurposeCodeCarm());
    }

    @Test
    void shouldCreateAndDeleteFacilityInJsonStore() {

        service.saveFacilityType(
                "FT999",
                "Test Facility",
                "Y",
                "N",
                true);

        assertEquals(
                4,
                service.getFacilityTypes().size());

        service.deleteFacilityType("FT999");

        assertEquals(
                3,
                service.getFacilityTypes().size());
    }

    @Test
    void shouldUpdateFacilityIndicatorsInJsonStore() {

        FacilityTypeIndicatorRequest request =
                new FacilityTypeIndicatorRequest();

        request.setAdvised("N");
        request.setCommitted("N");

        service.updateFacilityTypeIndicators(
                "FT001",
                request);

        assertEquals(
                "N",
                service.getFacilityType("FT001")
                        .getAdvised());
    }

    @Test
    void shouldRejectDuplicateFacilityType() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.saveFacilityType(
                        "FT001",
                        "Duplicate",
                        "Y",
                        "Y",
                        true));
    }
}
