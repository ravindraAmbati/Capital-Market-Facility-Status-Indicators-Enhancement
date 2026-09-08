package com.sab.fcm.carm.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sab.fcm.carm.client.FacilityCapitalMarkersRestClient;
import com.sab.fcm.carm.config.CarmFcmProperties;
import org.springframework.stereotype.Service;

@Service
public class ReferenceDataProviderImpl
        implements ReferenceDataProvider {

    private final FacilityCapitalMarkersRestClient restClient;
    private final CarmFcmProperties properties;
    private final ObjectMapper objectMapper;

    public ReferenceDataProviderImpl(
            FacilityCapitalMarkersRestClient restClient,
            CarmFcmProperties properties) {

        this.restClient = restClient;
        this.properties = properties;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public JsonNode loadReferenceData() {

        ObjectNode combined =
                objectMapper.createObjectNode();

        String configured =
                properties.getReferenceDataEndpoints();

        if (configured == null || configured.trim().isEmpty()) {
            throw new IllegalStateException(
                    "No FCM reference-data endpoints configured");
        }

        String[] definitions = configured.split(",");

        for (String definition : definitions) {
            String item = definition.trim();

            if (item.isEmpty()) {
                continue;
            }

            int separator = item.indexOf('=');

            if (separator <= 0 || separator == item.length() - 1) {
                throw new IllegalStateException(
                        "Invalid FCM reference-data endpoint: " + item);
            }

            String logicalName =
                    item.substring(0, separator).trim();

            String path =
                    item.substring(separator + 1).trim();

            if (logicalName.isEmpty() || path.isEmpty()) {
                throw new IllegalStateException(
                        "Invalid FCM reference-data endpoint: " + item);
            }

            if (restClient.isAdminApi(path)) {
                throw new IllegalStateException(
                        "Admin API cannot be configured as reference data: " + path);
            }

            JsonNode response = restClient.get(path);

            if (response == null) {
                throw new IllegalStateException(
                        "FCM returned null for reference-data endpoint: " + path);
            }

            combined.set(logicalName, response);
        }

        return combined;
    }
}
