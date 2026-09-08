package com.sab.fcm.carm.client;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface FacilityCapitalMarkersRestClient {

    JsonNode get(String path);

    JsonNode get(String path, Map<String, String> queryParameters);

    JsonNode post(String path, JsonNode request);

    JsonNode put(String path, JsonNode request);

    JsonNode delete(String path);

    boolean isAdminApi(String path);
}
