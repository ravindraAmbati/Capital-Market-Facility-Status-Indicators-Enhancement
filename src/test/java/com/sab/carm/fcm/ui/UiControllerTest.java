package com.sab.carm.fcm.ui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UiControllerTest {

    private final UiController controller = new UiController();

    @Test
    void rootShouldRedirectToMaintenance() {
        assertEquals("redirect:/maintenance", controller.home());
    }

    @Test
    void maintenanceShouldReturnMaintenanceView() {
        assertEquals("maintenance", controller.maintenance());
    }

    @Test
    void accessDeniedShouldReturn403View() {
        assertEquals("403", controller.accessDenied());
    }
}
