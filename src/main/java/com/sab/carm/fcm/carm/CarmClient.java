package com.sab.carm.fcm.carm;

import com.sab.carm.fcm.carm.dto.CarmReferenceDataRequest;
import com.sab.carm.fcm.carm.dto.CarmReferenceDataResponse;
import java.net.URI;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CarmClient {

    private final RestTemplate restTemplate;
    private final CarmProperties properties;

    public CarmClient(
            RestTemplate restTemplate,
            CarmProperties properties) {

        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public CarmReferenceDataResponse fetchReferenceData(
            String tableName) {

        String url =
                properties.getApi().getBaseUrl()
                        + properties.getApi().getReferenceDataPath();

        CarmReferenceDataRequest request =
                new CarmReferenceDataRequest(
                        properties.getSiteId(),
                        tableName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                MediaType.APPLICATION_JSON);

        HttpEntity<CarmReferenceDataRequest> entity =
                new HttpEntity<>(request, headers);

        ResponseEntity<CarmReferenceDataResponse> response =
                restTemplate.exchange(
                        URI.create(url),
                        HttpMethod.POST,
                        entity,
                        CarmReferenceDataResponse.class);

        return response.getBody();
    }
}