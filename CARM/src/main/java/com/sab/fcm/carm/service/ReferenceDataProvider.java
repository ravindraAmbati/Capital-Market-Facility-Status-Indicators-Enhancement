package com.sab.fcm.carm.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface ReferenceDataProvider {
    JsonNode loadReferenceData();
}
