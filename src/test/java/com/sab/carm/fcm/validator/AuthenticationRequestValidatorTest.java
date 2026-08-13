package com.sab.carm.fcm.validator;

import static org.assertj.core.api.Assertions.assertThat;

import com.sab.carm.fcm.dto.AuthenticationRequest;
import org.junit.jupiter.api.Test;

class AuthenticationRequestValidatorTest {

    @Test
    void validatesRequiredFields() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("svc_api");
        request.setPassword("secret");

        assertThat(new AuthenticationRequestValidator().isValid(request)).isTrue();
        assertThat(new AuthenticationRequestValidator().isValid(new AuthenticationRequest())).isFalse();
    }
}
