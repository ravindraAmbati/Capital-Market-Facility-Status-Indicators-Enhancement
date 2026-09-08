package com.sab.fcm.carm.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sab.fcm.carm.config.CarmFcmProperties;
import com.sab.fcm.carm.security.CredentialService;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;


import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.springframework.http.MediaType;

public class FacilityCapitalMarkersRestClientImplTest {

    @Test
    public void shouldCallGetAndParseJson() {

        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server =
                MockRestServiceServer.createServer(restTemplate);

        CarmFcmProperties properties = mock(CarmFcmProperties.class);
        when(properties.getBaseUrl()).thenReturn("http://fcm");
        when(properties.getUsername()).thenReturn("sa-svc-carm-fcm-api");
        when(properties.getEncryptedPassword()).thenReturn("ENC(password)");
        when(properties.getAdminApiPrefix()).thenReturn("/api/admin");

        CredentialService credentials = mock(CredentialService.class);
        when(credentials.decrypt("ENC(password)")).thenReturn("secret");

        FacilityCapitalMarkersRestClient client =
                new FacilityCapitalMarkersRestClientImpl(
                        restTemplate,
                        properties,
                        credentials);

        server.expect(requestTo("http://fcm/api/reference/data"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        "{\"facilityTypes\":[]}",
                        MediaType.APPLICATION_JSON));

        JsonNode result = client.get("/api/reference/data");

        assertNotNull(result);
        assertTrue(result.has("facilityTypes"));

        server.verify();
    }

    @Test
    public void shouldCallGetWithQueryParameters() {

        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server =
                MockRestServiceServer.createServer(restTemplate);

        CarmFcmProperties properties = mock(CarmFcmProperties.class);
        when(properties.getBaseUrl()).thenReturn("http://fcm");
        when(properties.getUsername()).thenReturn("sa-svc-carm-fcm-api");
        when(properties.getEncryptedPassword()).thenReturn("secret");
        when(properties.getAdminApiPrefix()).thenReturn("/api/admin");

        CredentialService credentials = mock(CredentialService.class);
        when(credentials.decrypt("secret")).thenReturn("secret");

        FacilityCapitalMarkersRestClient client =
                new FacilityCapitalMarkersRestClientImpl(
                        restTemplate,
                        properties,
                        credentials);

        server.expect(requestTo("http://fcm/api/data?relationshipId=123&serialNo=456"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        "{\"ok\":true}",
                        MediaType.APPLICATION_JSON));

        JsonNode result =
                client.get(
                        "/api/data",
                        new java.util.LinkedHashMap<String, String>() {{
                            put("relationshipId", "123");
                            put("serialNo", "456");
                        }});

        assertTrue(result.get("ok").asBoolean());
        server.verify();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectAdminApi() {

        RestTemplate restTemplate = new RestTemplate();
        CarmFcmProperties properties = mock(CarmFcmProperties.class);
        when(properties.getBaseUrl()).thenReturn("http://fcm");
        when(properties.getUsername()).thenReturn("user");
        when(properties.getEncryptedPassword()).thenReturn("secret");
        when(properties.getAdminApiPrefix()).thenReturn("/api/admin");

        CredentialService credentials = mock(CredentialService.class);
        when(credentials.decrypt("secret")).thenReturn("secret");

        FacilityCapitalMarkersRestClient client =
                new FacilityCapitalMarkersRestClientImpl(
                        restTemplate,
                        properties,
                        credentials);

        client.get("/api/admin/facility-types");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectEmptyPath() {

        RestTemplate restTemplate = new RestTemplate();
        CarmFcmProperties properties = mock(CarmFcmProperties.class);
        when(properties.getBaseUrl()).thenReturn("http://fcm");
        when(properties.getUsername()).thenReturn("user");
        when(properties.getEncryptedPassword()).thenReturn("secret");
        when(properties.getAdminApiPrefix()).thenReturn("/api/admin");

        CredentialService credentials = mock(CredentialService.class);
        when(credentials.decrypt("secret")).thenReturn("secret");

        FacilityCapitalMarkersRestClient client =
                new FacilityCapitalMarkersRestClientImpl(
                        restTemplate,
                        properties,
                        credentials);

        client.get("");
    }
}
