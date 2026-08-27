package com.sab.carm.fcm.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class DefaultsControllerOpenApiContractTest {

    @Test
    void defaultsEndpointShouldHaveOpenApiMetadata() throws Exception {
        Method method = DefaultsController.class.getMethod(
                "getDefaults", String.class);

        Operation operation = method.getAnnotation(Operation.class);

        assertNotNull(operation);
        assertEquals(
                "Get complete maintenance defaults",
                operation.summary());
    }

    @Test
    void defaultsEndpointShouldUseCanonicalPath() throws Exception {
        org.springframework.web.bind.annotation.RequestMapping mapping =
                DefaultsController.class.getAnnotation(
                        org.springframework.web.bind.annotation.RequestMapping.class);

        assertNotNull(mapping);
        assertEquals("/api/carm/fcm", mapping.value()[0]);

        Method method = DefaultsController.class.getMethod(
                "getDefaults", String.class);

        org.springframework.web.bind.annotation.GetMapping getMapping =
                method.getAnnotation(
                        org.springframework.web.bind.annotation.GetMapping.class);

        assertNotNull(getMapping);
        assertEquals("/defaults", getMapping.value()[0]);
    }
}
