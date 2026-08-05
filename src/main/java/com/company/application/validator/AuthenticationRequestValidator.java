package com.company.application.validator;

import com.company.application.dto.AuthenticationRequest;
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
