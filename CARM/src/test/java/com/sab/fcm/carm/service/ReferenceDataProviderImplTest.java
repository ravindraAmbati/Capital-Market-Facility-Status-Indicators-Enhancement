package com.sab.fcm.carm.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sab.fcm.carm.client.FacilityCapitalMarkersRestClient;
import com.sab.fcm.carm.config.CarmFcmProperties;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReferenceDataProviderImplTest {

    @Test
    public void shouldCombineConfiguredReferenceData() {

        FacilityCapitalMarkersRestClient client =
                mock(FacilityCapitalMarkersRestClient.class);

        CarmFcmProperties properties =
                mock(CarmFcmProperties.class);

        when(properties.getReferenceDataEndpoints())
                .thenReturn(
                        "facilityTypes=/api/reference/facility-types," +
                        "purposeCodes=/api/reference/purpose-codes");

        ObjectMapper mapper = new ObjectMapper();

        when(client.isAdminApi(anyString())).thenReturn(false);

        when(client.get("/api/reference/facility-types"))
                .thenReturn(mapper.createObjectNode().put("count", 2));

        when(client.get("/api/reference/purpose-codes"))
                .thenReturn(mapper.createObjectNode().put("count", 5));

        ReferenceDataProviderImpl provider =
                new ReferenceDataProviderImpl(client, properties);

        JsonNode result = provider.loadReferenceData();

        assertEquals(
                2,
                result.get("facilityTypes")
                        .get("count").asInt());

        assertEquals(
                5,
                result.get("purposeCodes")
                        .get("count").asInt());

        verify(client).get("/api/reference/facility-types");
        verify(client).get("/api/reference/purpose-codes");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRejectAdminReferenceEndpoint() {

        FacilityCapitalMarkersRestClient client =
                mock(FacilityCapitalMarkersRestClient.class);

        CarmFcmProperties properties =
                mock(CarmFcmProperties.class);

        when(properties.getReferenceDataEndpoints())
                .thenReturn(
                        "facilityTypes=/api/admin/facility-types");

        when(client.isAdminApi("/api/admin/facility-types"))
                .thenReturn(true);

        ReferenceDataProviderImpl provider =
                new ReferenceDataProviderImpl(client, properties);

        provider.loadReferenceData();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRejectMalformedEndpoint() {

        FacilityCapitalMarkersRestClient client =
                mock(FacilityCapitalMarkersRestClient.class);

        CarmFcmProperties properties =
                mock(CarmFcmProperties.class);

        when(properties.getReferenceDataEndpoints())
                .thenReturn("facilityTypes");

        ReferenceDataProviderImpl provider =
                new ReferenceDataProviderImpl(client, properties);

        provider.loadReferenceData();
    }
}
