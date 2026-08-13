package com.sab.carm.fcm.validator;

import com.sab.carm.fcm.dto.AuthenticationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Validates authentication request bodies.
 */
@Component
public class AuthenticationRequestValidator {

    public boolean isValid(AuthenticationRequest request) {
        return request != null
                && StringUtils.hasText(request.getUsername())
                && StringUtils.hasText(request.getPassword());
    }
}
