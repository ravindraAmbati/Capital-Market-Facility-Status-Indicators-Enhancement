package com.sab.carm.fcm.config;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductionConfigurationTest {

    @Test
    void productionConfigurationShouldNotExposeEnvironmentDetails()
            throws IOException {

        String yaml = readApplicationYaml();

        assertTrue(yaml.contains(
                "show-details: when_authorized"));

        assertTrue(yaml.contains(
                "enabled: false"));
    }

    @Test
    void productionConfigurationShouldKeepMongoAutoIndexCreationDisabled()
            throws IOException {

        String yaml = readApplicationYaml();

        assertTrue(yaml.contains(
                "auto-index-creation: false"));
    }

    @Test
    void productionConfigurationShouldNotConfigureReportCollection()
            throws IOException {

        String yaml = readApplicationYaml();

        assertFalse(yaml.contains(
                "creditApplicationCapitalMarkersReport:"));
    }

    @Test
    void productionConfigurationShouldUseInfoLogging()
            throws IOException {

        String yaml = readApplicationYaml();

        assertTrue(yaml.contains(
                "com.sab.carm.fcm: INFO"));

        assertFalse(yaml.contains(
                "com.sab.carm.fcm: DEBUG"));
    }

    @Test
    void productionConfigurationShouldEnableThymeleafCaching()
            throws IOException {

        String yaml = readApplicationYaml();

        assertTrue(yaml.contains(
                "thymeleaf:\n    cache: true"));
    }

    private String readApplicationYaml()
            throws IOException {

        ClassPathResource resource =
                new ClassPathResource(
                        "application.yml");

        return new String(
                resource.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8);
    }
}
