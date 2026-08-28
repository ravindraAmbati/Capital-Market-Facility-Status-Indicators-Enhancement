package com.sab.carm.fcm.ui;

import com.sab.carm.fcm.service.MaintenanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class MaintenanceUiControllerTest {

    private MaintenanceUiController controller;
    private MaintenanceService maintenanceService;

    @BeforeEach
    void setUp() {
        maintenanceService = mock(MaintenanceService.class);
        controller = new MaintenanceUiController(
                maintenanceService);
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
}
