package com.sab.carm.fcm.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.naming.AuthenticationException;
import javax.naming.directory.DirContext;
import org.junit.jupiter.api.Test;

class LdapAuthenticationServiceTest {

    @Test
    void authenticatesWhenSearchAndPasswordValidationSucceed() throws Exception {
        LdapConnectionFactory factory = mock(LdapConnectionFactory.class);
        LdapSearchService searchService = mock(LdapSearchService.class);
        DirContext searchContext = mock(DirContext.class);
        DirContext validationContext = mock(DirContext.class);
        when(factory.createAnonymousContext()).thenReturn(searchContext);
        when(searchService.findDistinguishedName(searchContext, "user1")).thenReturn("cn=user1");
        when(factory.createAuthenticatedContext("cn=user1", "secret")).thenReturn(validationContext);

        LdapAuthenticationService service = new LdapAuthenticationService(factory, searchService);

        assertThat(service.authenticate("user1", "secret")).isTrue();
        verify(searchContext).close();
        verify(validationContext).close();
    }

    @Test
    void rejectsWhenPasswordValidationFails() throws Exception {
        LdapConnectionFactory factory = mock(LdapConnectionFactory.class);
        LdapSearchService searchService = mock(LdapSearchService.class);
        DirContext searchContext = mock(DirContext.class);
        when(factory.createAnonymousContext()).thenReturn(searchContext);
        when(searchService.findDistinguishedName(searchContext, "user1")).thenReturn("cn=user1");
        when(factory.createAuthenticatedContext("cn=user1", "bad")).thenThrow(new AuthenticationException());

        LdapAuthenticationService service = new LdapAuthenticationService(factory, searchService);

        assertThat(service.authenticate("user1", "bad")).isFalse();
    }
}
