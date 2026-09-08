package com.sab.fcm.carm.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sab.fcm.carm.cache.ReferenceDataCache;
import com.sab.fcm.carm.client.FacilityCapitalMarkersRestClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CarmFcmMasterServiceImpl
        implements CarmFcmMasterService {

    private final ReferenceDataCache referenceDataCache;
    private final FacilityCapitalMarkersRestClient restClient;

    public CarmFcmMasterServiceImpl(
            ReferenceDataCache referenceDataCache,
            FacilityCapitalMarkersRestClient restClient) {

        this.referenceDataCache = referenceDataCache;
        this.restClient = restClient;
    }

    @Override
    public JsonNode getReferenceData() {
        return referenceDataCache.get();
    }

    @Override
    public JsonNode get(String path) {
        return restClient.get(path);
    }

    @Override
    public JsonNode get(
            String path,
            Map<String, String> queryParameters) {
        return restClient.get(path, queryParameters);
    }

    @Override
    public JsonNode post(String path, JsonNode request) {
        return restClient.post(path, request);
    }

    @Override
    public JsonNode put(String path, JsonNode request) {
        return restClient.put(path, request);
    }

    @Override
    public JsonNode delete(String path) {
        return restClient.delete(path);
    }

    @Override
    public void refreshReferenceData() {
        referenceDataCache.refreshNow();
    }

    @Override
    public void invalidateReferenceData() {
        referenceDataCache.invalidate();
    }
}
