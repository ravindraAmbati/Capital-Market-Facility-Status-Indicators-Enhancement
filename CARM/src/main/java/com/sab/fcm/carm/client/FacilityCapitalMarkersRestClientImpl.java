package com.sab.fcm.carm.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sab.fcm.carm.config.CarmFcmProperties;
import com.sab.fcm.carm.security.CredentialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

@Component
public class FacilityCapitalMarkersRestClientImpl
        implements FacilityCapitalMarkersRestClient {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(FacilityCapitalMarkersRestClientImpl.class);

    private final RestTemplate restTemplate;
    private final CarmFcmProperties properties;
    private final ObjectMapper objectMapper;
    private final String authorizationHeader;

    public FacilityCapitalMarkersRestClientImpl(
            RestTemplate restTemplate,
            CarmFcmProperties properties,
            CredentialService credentialService) {

        this.restTemplate = restTemplate;
        this.properties = properties;
        this.objectMapper = new ObjectMapper();

        String password =
                credentialService.decrypt(properties.getEncryptedPassword());

        if (password == null) {
            throw new IllegalStateException(
                    "Facility Capital Markers password is not configured");
        }

        String credentials =
                properties.getUsername() + ":" + password;

        authorizationHeader =
                "Basic " +
                java.util.Base64.getEncoder().encodeToString(
                        credentials.getBytes(Charset.forName("UTF-8")));
    }

    @Override
    public JsonNode get(String path) {
        return exchange(
                HttpMethod.GET,
                path,
                Collections.<String, String>emptyMap(),
                null);
    }

    @Override
    public JsonNode get(
            String path,
            Map<String, String> queryParameters) {
        return exchange(
                HttpMethod.GET,
                path,
                queryParameters,
                null);
    }

    @Override
    public JsonNode post(String path, JsonNode request) {
        return exchange(
                HttpMethod.POST,
                path,
                Collections.<String, String>emptyMap(),
                request);
    }

    @Override
    public JsonNode put(String path, JsonNode request) {
        return exchange(
                HttpMethod.PUT,
                path,
                Collections.<String, String>emptyMap(),
                request);
    }

    @Override
    public JsonNode delete(String path) {
        return exchange(
                HttpMethod.DELETE,
                path,
                Collections.<String, String>emptyMap(),
                null);
    }

    @Override
    public boolean isAdminApi(String path) {
        if (path == null) {
            return false;
        }

        String normalized =
                path.startsWith("/") ? path : "/" + path;

        String prefix = properties.getAdminApiPrefix();

        if (prefix == null || prefix.trim().isEmpty()) {
            return false;
        }

        prefix = prefix.startsWith("/") ? prefix : "/" + prefix;

        return normalized.equals(prefix)
                || normalized.startsWith(prefix + "/");
    }

    private JsonNode exchange(
            HttpMethod method,
            String path,
            Map<String, String> queryParameters,
            JsonNode request) {

        validatePath(path);

        URI uri = buildUri(path, queryParameters);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, authorizationHeader);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        if (request != null) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }

        HttpEntity<JsonNode> entity =
                new HttpEntity<JsonNode>(request, headers);

        try {
            LOGGER.debug(
                    "Calling Facility Capital Markers API: {} {}",
                    method,
                    path);

            ResponseEntity<String> response =
                    restTemplate.exchange(
                            uri,
                            method,
                            entity,
                            String.class);

            String body = response.getBody();

            if (body == null || body.trim().isEmpty()) {
                return objectMapper.createObjectNode();
            }

            return objectMapper.readTree(body);

        } catch (HttpStatusCodeException e) {
            LOGGER.error(
                    "Facility Capital Markers API failed: {} {} HTTP {}",
                    method,
                    path,
                    e.getStatusCode().value(),
                    e);

            throw new FcmRestClientException(
                    "Facility Capital Markers returned HTTP " +
                    e.getStatusCode().value() +
                    " for " + method + " " + path,
                    e.getStatusCode().value(),
                    e);

        } catch (RestClientException e) {
            LOGGER.error(
                    "Facility Capital Markers REST call failed: {} {}",
                    method,
                    path,
                    e);

            throw new FcmRestClientException(
                    "Facility Capital Markers REST call failed for " +
                    method + " " + path,
                    e);

        } catch (Exception e) {
            LOGGER.error(
                    "Unable to process Facility Capital Markers response: {} {}",
                    method,
                    path,
                    e);

            throw new FcmRestClientException(
                    "Unable to process Facility Capital Markers response",
                    e);
        }
    }

    private URI buildUri(
            String path,
            Map<String, String> queryParameters) {

        String normalizedPath =
                path.startsWith("/") ? path : "/" + path;

        UriComponentsBuilder builder =
                UriComponentsBuilder
                        .fromHttpUrl(properties.getBaseUrl())
                        .path(normalizedPath);

        if (queryParameters != null) {
            for (Map.Entry<String, String> entry
                    : queryParameters.entrySet()) {
                builder.queryParam(
                        entry.getKey(),
                        entry.getValue());
            }
        }

        return builder.build().encode().toUri();
    }

    private void validatePath(String path) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Facility Capital Markers API path cannot be empty");
        }

        if (isAdminApi(path)) {
            throw new IllegalArgumentException(
                    "Administrative Facility Capital Markers APIs " +
                    "are not available through CARM integration: " + path);
        }
    }
}
