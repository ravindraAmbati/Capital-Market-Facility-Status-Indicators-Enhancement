package com.sab.carm.fcm.ui;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityConfigUiContractTest {

    private static final String PATH =
        "src/main/java/com/sab/carm/fcm/config/SecurityConfig.java";

    @Test
    void shouldConfigureFormLoginAndMaintenanceRedirect() throws Exception {
        String source = read();
        assertTrue(source.contains(".formLogin()"));
        assertTrue(source.contains(".defaultSuccessUrl(\"/maintenance\", true)"));
        assertTrue(source.contains(".failureUrl(\"/login?error=true\")"));
    }

    @Test
    void shouldConfigureLogoutAndAccessDenied() throws Exception {
        String source = read();
        assertTrue(source.contains(".logout()"));
        assertTrue(source.contains(".invalidateHttpSession(true)"));
        assertTrue(source.contains(".clearAuthentication(true)"));
        assertTrue(source.contains(".accessDeniedPage(\"/403\")"));
    }

    @Test
    void maintenanceShouldRequireAdminRole() throws Exception {
        String source = read();
        assertTrue(source.contains(".antMatchers(\"/maintenance\")"));
        assertTrue(source.contains(".hasRole(\"ADMIN\")"));
    }

    private String read() throws Exception {
        return new String(Files.readAllBytes(Paths.get(PATH)), "UTF-8");
    }
}
