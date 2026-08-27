package com.sab.carm.fcm.dto.integration;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreditApplicationConsentRequestValidationTest {

    private final Validator validator =
            Validation
                    .buildDefaultValidatorFactory()
                    .getValidator();

    @Test
    void validConsentShouldPass() {

        CreditApplicationConsentRequest request =
                new CreditApplicationConsentRequest();

        request.setRelationshipId("REL001");
        request.setSerialNo("001");
        request.setDecision(DecisionType.RECOMMEND);
        request.setHubUserId("HUBUSER01");

        assertTrue(
                validator.validate(request).isEmpty());
    }

    @Test
    void missingUserShouldFail() {

        CreditApplicationConsentRequest request =
                new CreditApplicationConsentRequest();

        request.setRelationshipId("REL001");
        request.setSerialNo("001");
        request.setDecision(DecisionType.RECOMMEND);
        request.setHubUserId("");

        assertFalse(
                validator.validate(request).isEmpty());
    }

    @Test
    void missingDecisionShouldFail() {

        CreditApplicationConsentRequest request =
                new CreditApplicationConsentRequest();

        request.setRelationshipId("REL001");
        request.setSerialNo("001");
        request.setHubUserId("HUBUSER01");

        assertFalse(
                validator.validate(request).isEmpty());
    }
}
