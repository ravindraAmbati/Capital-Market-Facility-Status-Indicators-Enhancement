package com.company.application.authorization;

import static org.assertj.core.api.Assertions.assertThat;

import com.company.application.constants.SecurityConstants;
import com.company.application.security.SecurityRoleProperties;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class AuthorizationServiceTest {

    @Test
    void resolvesConfiguredRoles() {
        SecurityRoleProperties properties = new SecurityRoleProperties();
        properties.setAdmin(Arrays.asList("admin1"));
        properties.setApi(Arrays.asList("svc_api"));
        properties.setReadonly(Arrays.asList("reader"));

        AuthorizationService service = new AuthorizationService(properties);

        assertThat(service.rolesFor("admin1")).containsExactly(SecurityConstants.ROLE_ADMIN);
        assertThat(service.hasRole("svc_api", SecurityConstants.ROLE_API)).isTrue();
        assertThat(service.rolesFor("unknown")).isEmpty();
    }
}
