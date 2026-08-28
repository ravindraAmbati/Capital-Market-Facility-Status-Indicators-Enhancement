package com.sab.carm.fcm.ui;

import com.sab.carm.fcm.repository.FacilityTypeMaintenanceRepository;
import com.sab.carm.fcm.repository.PurposeCodeMaintenanceRepository;
import com.sab.carm.fcm.service.MaintenanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class MaintenanceUiControllerTest {

    private MaintenanceUiController controller;

    @Mock
    private MaintenanceService maintenanceService;

    private FacilityTypeMaintenanceRepository facilityTypeRepository;
    private PurposeCodeMaintenanceRepository purposeCodeRepository;

    @BeforeEach
    void setUp() {
        maintenanceService = mock(MaintenanceService.class);
        facilityTypeRepository =
                mock(FacilityTypeMaintenanceRepository.class);
        purposeCodeRepository =
                mock(PurposeCodeMaintenanceRepository.class);

        controller = new MaintenanceUiController(
                maintenanceService,
                facilityTypeRepository,
                purposeCodeRepository);
    }

    @Test
    void maintenancePageShouldReturnMaintenanceView() {

        org.springframework.ui.Model model =
                new org.springframework.ui.ExtendedModelMap();

        assertEquals(
                "maintenance",
                controller.maintenance(
                        null,
                        null,
                        null,
                        model));
    }

    @Test
    void deleteConfirmationShouldUseExpectedMessagePattern() {
        assertEquals(
                "redirect:/maintenance?type=SUCCESS&message="
                        + "Facility+type+deleted+successfully.",
                invokeSuccess(
                        "Facility type deleted successfully."));
    }

    private String invokeSuccess(String message) {
        try {
            java.lang.reflect.Method method =
                    MaintenanceUiController.class
                            .getDeclaredMethod(
                                    "redirectSuccess",
                                    String.class);
            method.setAccessible(true);
            return (String) method.invoke(
                    controller,
                    message);
        } catch (Exception exception) {
            throw new AssertionError(exception);
        }
    }
}
