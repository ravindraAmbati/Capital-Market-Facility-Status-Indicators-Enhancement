package com.sab.carm.fcm.authorization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sab.carm.fcm.constants.SecurityConstants;
import com.sab.carm.fcm.security.SecurityRoleProperties;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthorizationServiceTest {

    private AuthorizationService authorizationService;

    @BeforeEach
    void setUp() {

        SecurityRoleProperties properties =
                new SecurityRoleProperties();

        properties.setAdmin(Arrays.asList(
                "sa-svc-carm-admin"));

        properties.setApi(Arrays.asList(
                "sa-svc-carm-api"));

        properties.setAudit(Arrays.asList(
                "sa-svc-carm-audit"));

        properties.setItsup(Arrays.asList(
                "sa-svc-carm-itsup"));

        authorizationService =
                new AuthorizationService(properties);
    }

    @Test
    void shouldResolveAdminRole() {

        assertEquals(
                Arrays.asList(SecurityConstants.ROLE_ADMIN),
                authorizationService.rolesFor(
                        "sa-svc-carm-admin"));
    }

    @Test
    void shouldResolveApiRole() {

        assertEquals(
                Arrays.asList(SecurityConstants.ROLE_API),
                authorizationService.rolesFor(
                        "sa-svc-carm-api"));
    }

    @Test
    void shouldResolveAuditRole() {

        assertEquals(
                Arrays.asList(SecurityConstants.ROLE_AUDIT),
                authorizationService.rolesFor(
                        "sa-svc-carm-audit"));
    }

    @Test
    void shouldResolveItsupRole() {

        assertEquals(
                Arrays.asList(SecurityConstants.ROLE_ITSUP),
                authorizationService.rolesFor(
                        "sa-svc-carm-itsup"));
    }

    @Test
    void shouldRejectUnknownUser() {

        assertTrue(
                authorizationService
                        .rolesFor("unknown-user")
                        .isEmpty());

        assertFalse(
                authorizationService
                        .isAuthorized("unknown-user"));
    }

    @Test
    void shouldAllowReadForAuditUser() {

        assertTrue(
                authorizationService.hasPermission(
                        "sa-svc-carm-audit",
                        SecurityConstants.PERMISSION_READ));
    }

    @Test
    void shouldNotAllowWriteForAuditUser() {

        assertFalse(
                authorizationService.hasPermission(
                        "sa-svc-carm-audit",
                        SecurityConstants.PERMISSION_WRITE));
    }

    @Test
    void shouldAllowWriteForAdmin() {

        assertTrue(
                authorizationService.hasPermission(
                        "sa-svc-carm-admin",
                        SecurityConstants.PERMISSION_WRITE));
    }

    @Test
    void shouldAllowWriteForApiUser() {

        assertTrue(
                authorizationService.hasPermission(
                        "sa-svc-carm-api",
                        SecurityConstants.PERMISSION_WRITE));
    }
}