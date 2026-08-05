package com.company.application.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.company.application.constants.SecurityConstants;
import com.company.application.exception.SecurityException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class TokenServiceTest {

    @Test
    void validatesGeneratedToken() {
        TokenProperties properties = new TokenProperties();
        TokenService service = new TokenService(properties, Clock.fixed(Instant.parse("2026-08-05T00:00:00Z"), ZoneOffset.UTC));

        String token = service.generateToken("svc_api", Arrays.asList(SecurityConstants.ROLE_API), "origin");

        assertThat(service.validateToken(token, "other").getUsername()).isEqualTo("svc_api");
    }

    @Test
    void rejectsInvalidToken() {
        TokenService service = new TokenService(new TokenProperties(), Clock.systemUTC());

        assertThatThrownBy(() -> service.validateToken("missing", null)).isInstanceOf(SecurityException.class);
    }

    @Test
    void enforcesSameOriginWhenEnabled() {
        TokenProperties properties = new TokenProperties();
        properties.setSameOriginRequired(true);
        TokenService service = new TokenService(properties, Clock.systemUTC());
        String token = service.generateToken("svc_api", Arrays.asList(SecurityConstants.ROLE_API), "https://a");

        assertThatThrownBy(() -> service.validateToken(token, "https://b")).isInstanceOf(SecurityException.class);
    }
}
