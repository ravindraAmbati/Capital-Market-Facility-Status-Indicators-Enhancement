package com.sab.fcm.carm.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface CarmFcmMasterService {

    JsonNode getReferenceData();

    JsonNode get(String path);

    JsonNode get(String path, Map<String, String> queryParameters);

    JsonNode post(String path, JsonNode request);

    JsonNode put(String path, JsonNode request);

    JsonNode delete(String path);

    void refreshReferenceData();

    void invalidateReferenceData();
}
