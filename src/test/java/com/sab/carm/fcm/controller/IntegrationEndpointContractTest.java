package com.sab.carm.fcm.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegrationEndpointContractTest {

    @Test
    void facilityControllerMustExposeExpectedPath() {
        assertTrue(
                FacilityCapitalMarkersController.class
                        .isAnnotationPresent(RequestMapping.class));

        RequestMapping mapping =
                FacilityCapitalMarkersController.class
                        .getAnnotation(RequestMapping.class);

        assertTrue(
                java.util.Arrays.asList(mapping.value())
                        .contains("/api/carm/fcm/facility"));
    }

    @Test
    void reportControllerMustExposeExpectedPath() {
        assertTrue(
                CreditApplicationReportController.class
                        .isAnnotationPresent(RequestMapping.class));

        RequestMapping mapping =
                CreditApplicationReportController.class
                        .getAnnotation(RequestMapping.class);

        assertTrue(
                java.util.Arrays.asList(mapping.value())
                        .contains("/api/carm/fcm/report"));
    }

    @Test
    void consentControllerMustExposeExpectedPath() {
        assertTrue(
                CreditApplicationConsentController.class
                        .isAnnotationPresent(RequestMapping.class));

        RequestMapping mapping =
                CreditApplicationConsentController.class
                        .getAnnotation(RequestMapping.class);

        assertTrue(
                java.util.Arrays.asList(mapping.value())
                        .contains("/api/carm/fcm/creditapplication"));
    }

    @Test
    void facilityControllerHasGetPostDeleteOperations() {
        long get = countMethods(
                FacilityCapitalMarkersController.class,
                "GetMapping");
        long post = countMethods(
                FacilityCapitalMarkersController.class,
                "PostMapping");
        long delete = countMethods(
                FacilityCapitalMarkersController.class,
                "DeleteMapping");

        assertTrue(get > 0);
        assertTrue(post > 0);
        assertTrue(delete > 0);
    }

    private long countMethods(
            Class<?> type,
            String annotationSimpleName) {

        return java.util.Arrays.stream(type.getDeclaredMethods())
                .filter(method ->
                        java.util.Arrays.stream(
                                        method.getAnnotations())
                                .anyMatch(annotation ->
                                        annotation.annotationType()
                                                .getSimpleName()
                                                .equals(annotationSimpleName)))
                .count();
    }
}
