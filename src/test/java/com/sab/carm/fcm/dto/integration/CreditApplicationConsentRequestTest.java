package com.sab.carm.fcm.dto.integration;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreditApplicationConsentRequestTest {

    private final Validator validator;

    CreditApplicationConsentRequestTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validConsentShouldHaveNoValidationErrors() {
        CreditApplicationConsentRequest request = new CreditApplicationConsentRequest();
        request.setRelationshipId("REL001");
        request.setSerialNo("001");
        request.setDecision(DecisionType.RECOMMEND);
        request.setHubUserId("AB12");

        assertEquals(0, validator.validate(request).size());
    }

    @Test
    void missingDecisionShouldFailValidation() {
        CreditApplicationConsentRequest request = new CreditApplicationConsentRequest();
        request.setRelationshipId("REL001");
        request.setSerialNo("001");
        request.setHubUserId("AB12");

        assertEquals(1, validator.validateProperty(request, "decision").size());
    }
}
