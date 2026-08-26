package com.sab.carm.fcm.config;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityConfigAuthorizationTest {

    private static final String PATH =
            "src/main/java/com/sab/carm/fcm/config/SecurityConfig.java";

    @Test
    void facilityPostMustAllowOnlyAdminAndApi() throws Exception {

        String source = read();

        assertTrue(source.contains(
                "HttpMethod.POST,\n"
                        + "                        \"/api/carm/fcm/facility\",\n"
                        + "                        \"/api/carm/fcm/creditapplication\""));

        assertTrue(source.contains(
                ".hasAnyRole(\n"
                        + "                        \"ADMIN\",\n"
                        + "                        \"API\")"));
    }

    @Test
    void facilityDeleteMustAllowApiForCarmIntegration()
            throws Exception {

        String source = read();

        assertTrue(source.contains(
                "HttpMethod.DELETE,\n"
                        + "                        \"/api/carm/fcm/facility\""));

        assertTrue(source.contains(
                "Facility deletion is part of the CARM-FCM"));
    }

    @Test
    void maintenanceIndicatorUpdateRemainsAdminOnly()
            throws Exception {

        String source = read();

        assertTrue(source.contains(
                "\"/api/maintenance/facility-types/*/indicators\""));

        assertTrue(source.contains(
                "\"/api/maintenance/purpose-codes/*/*/indicator\""));
    }

    private String read() throws Exception {
        return new String(
                Files.readAllBytes(Paths.get(PATH)),
                "UTF-8");
    }
}
