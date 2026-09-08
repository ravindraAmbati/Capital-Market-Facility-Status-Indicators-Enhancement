package com.sab.fcm.carm.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sab.fcm.carm.cache.ReferenceDataCache;
import com.sab.fcm.carm.client.FacilityCapitalMarkersRestClient;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class CarmFcmMasterServiceImplTest {

    @Test
    public void shouldUseCacheForReferenceData() {

        ReferenceDataCache cache = mock(ReferenceDataCache.class);
        FacilityCapitalMarkersRestClient client =
                mock(FacilityCapitalMarkersRestClient.class);

        JsonNode expected =
                new ObjectMapper().createObjectNode()
                        .put("facilityTypes", "data");

        when(cache.get()).thenReturn(expected);

        CarmFcmMasterServiceImpl service =
                new CarmFcmMasterServiceImpl(cache, client);

        assertSame(expected, service.getReferenceData());

        verify(cache).get();
        verifyNoMoreInteractions(client);
    }

    @Test
    public void shouldPassBusinessGetDirectlyToRestClient() {

        ReferenceDataCache cache = mock(ReferenceDataCache.class);
        FacilityCapitalMarkersRestClient client =
                mock(FacilityCapitalMarkersRestClient.class);

        JsonNode expected =
                new ObjectMapper().createObjectNode();

        when(client.get("/api/business"))
                .thenReturn(expected);

        CarmFcmMasterServiceImpl service =
                new CarmFcmMasterServiceImpl(cache, client);

        assertSame(expected, service.get("/api/business"));

        verify(client).get("/api/business");
        verifyNoMoreInteractions(cache);
    }

    @Test
    public void shouldPassWriteOperationsDirectlyToRestClient() {

        ReferenceDataCache cache = mock(ReferenceDataCache.class);
        FacilityCapitalMarkersRestClient client =
                mock(FacilityCapitalMarkersRestClient.class);

        JsonNode request =
                new ObjectMapper().createObjectNode()
                        .put("value", "x");

        JsonNode response =
                new ObjectMapper().createObjectNode()
                        .put("ok", true);

        when(client.post("/api/test", request))
                .thenReturn(response);

        when(client.put("/api/test", request))
                .thenReturn(response);

        when(client.delete("/api/test"))
                .thenReturn(response);

        assertSame(response, service(cache, client)
                .post("/api/test", request));

        assertSame(response, service(cache, client)
                .put("/api/test", request));

        assertSame(response, service(cache, client)
                .delete("/api/test"));

        verify(client).post("/api/test", request);
        verify(client).put("/api/test", request);
        verify(client).delete("/api/test");
    }

    @Test
    public void shouldRefreshAndInvalidateReferenceData() {

        ReferenceDataCache cache = mock(ReferenceDataCache.class);
        FacilityCapitalMarkersRestClient client =
                mock(FacilityCapitalMarkersRestClient.class);

        CarmFcmMasterService service =
                new CarmFcmMasterServiceImpl(cache, client);

        service.refreshReferenceData();
        service.invalidateReferenceData();

        verify(cache).refreshNow();
        verify(cache).invalidate();
    }

    private CarmFcmMasterServiceImpl service(
            ReferenceDataCache cache,
            FacilityCapitalMarkersRestClient client) {
        return new CarmFcmMasterServiceImpl(cache, client);
    }
}
