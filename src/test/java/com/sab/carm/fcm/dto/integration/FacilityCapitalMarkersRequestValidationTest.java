package com.sab.carm.fcm.dto.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FacilityCapitalMarkersRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator();
    }

    @Test
    void validRequestShouldHaveNoViolations() {

        FacilityCapitalMarkersRequest request =
                validRequest();

        Set<ConstraintViolation<
                FacilityCapitalMarkersRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void missingRequiredFacilityFieldsShouldFail() {

        FacilityCapitalMarkersRequest request =
                validRequest();

        request.setFacilityNo("");
        request.setFacilityType(null);
        request.setCarmPurposeCode("");

        Set<ConstraintViolation<
                FacilityCapitalMarkersRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void invalidIndicatorShouldFail() {

        FacilityCapitalMarkersRequest request =
                validRequest();

        request.getAdvised()
                .setIndicator("X");

        Set<ConstraintViolation<
                FacilityCapitalMarkersRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void overrideWithoutJustificationShouldFail() {

        FacilityCapitalMarkersRequest request =
                validRequest();

        request.getAdvised().setOverride(true);
        request.getAdvised()
                .setOverrideJustification(null);

        Set<ConstraintViolation<
                FacilityCapitalMarkersRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void overrideWithBlankJustificationShouldFail() {

        FacilityCapitalMarkersRequest request =
                validRequest();

        request.getCommitted().setOverride(true);
        request.getCommitted()
                .setOverrideJustification("   ");

        Set<ConstraintViolation<
                FacilityCapitalMarkersRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void overrideWithJustificationShouldPass() {

        FacilityCapitalMarkersRequest request =
                validRequest();

        request.getUnconditionalCancellable()
                .setOverride(true);
        request.getUnconditionalCancellable()
                .setOverrideJustification("Approved by business");

        Set<ConstraintViolation<
                FacilityCapitalMarkersRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    private FacilityCapitalMarkersRequest validRequest() {

        FacilityCapitalMarkersRequest request =
                new FacilityCapitalMarkersRequest();

        request.setCreditApplicationRelationshipId("REL001");
        request.setSerialNo("001");
        request.setFacilityNo("123");
        request.setFacilityType("1200");
        request.setCarmPurposeCode("PUR001");

        request.setAdvised(marker("Y"));
        request.setCommitted(marker("Y"));
        request.setUnconditionalCancellable(marker("Y"));

        return request;
    }

    private FacilityCapitalMarkersRequest.CapitalMarkerRequest marker(
            String indicator) {

        FacilityCapitalMarkersRequest.CapitalMarkerRequest marker =
                new FacilityCapitalMarkersRequest.CapitalMarkerRequest();

        marker.setIndicator(indicator);

        return marker;
    }
}
