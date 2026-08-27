package com.sab.carm.fcm.dto.integration;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FacilityCapitalMarkersRequestTest {

    private final Validator validator;

    FacilityCapitalMarkersRequestTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validRequestShouldHaveNoValidationErrors() {
        assertEquals(0, validator.validate(validRequest()).size());
    }

    @Test
    void missingFacilityNumberShouldFailValidation() {
        FacilityCapitalMarkersRequest request = validRequest();
        request.setFacilityNo(null);

        assertEquals(1, validator.validateProperty(request, "facilityNo").size());
    }

    @Test
    void invalidIndicatorShouldFailValidation() {
        FacilityCapitalMarkersRequest request = validRequest();
        FacilityCapitalMarkersRequest.CapitalMarkerRequest marker =
                new FacilityCapitalMarkersRequest.CapitalMarkerRequest();
        marker.setIndicator("X");
        request.setAdvised(marker);

        assertEquals(1, validator.validate(request).size());
    }

    private FacilityCapitalMarkersRequest validRequest() {
        FacilityCapitalMarkersRequest request = new FacilityCapitalMarkersRequest();
        request.setCreditApplicationRelationshipId("REL001");
        request.setSerialNo("001");
        request.setFacilityNo("123");
        request.setFacilityType("FT01");
        request.setCarmPurposeCode("PURP01");
        request.setAdvised(marker("Y"));
        request.setCommitted(marker("Y"));
        request.setUnconditionalCancellable(marker("Y"));
        return request;
    }

    private FacilityCapitalMarkersRequest.CapitalMarkerRequest marker(String indicator) {
        FacilityCapitalMarkersRequest.CapitalMarkerRequest marker =
                new FacilityCapitalMarkersRequest.CapitalMarkerRequest();
        marker.setIndicator(indicator);
        return marker;
    }
}
