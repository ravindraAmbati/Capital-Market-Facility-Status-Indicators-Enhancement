package com.sab.carm.fcm.carm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.sab.carm.fcm.carm.dto.CarmReferenceDataResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

class CarmClientTest {

    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private CarmClient carmClient;
    private CarmProperties properties;

    @BeforeEach
    void setUp() {

        restTemplate =
                new RestTemplateBuilder().build();

        mockServer =
                MockRestServiceServer
                        .bindTo(restTemplate)
                        .build();

        properties =
                new CarmProperties();

        properties.setSiteId("ABCD");

        properties.getApi().setBaseUrl(
                "https://carm-test.company.com");

        properties.getApi().setReferenceDataPath(
                "/CarmDataService/referenceTable/data");

        carmClient =
                new CarmClient(
                        restTemplate,
                        properties);
    }

    @Test
    void shouldFetchReferenceData() {

        String expectedRequest =
                "{"
                        + "\"header\":{\"siteId\":\"ABCD\"},"
                        + "\"body\":{\"tableName\":\"1020\"}"
                        + "}";

        String response =
                "{"
                        + "\"header\":{},"
                        + "\"body\":{"
                        + "\"referenceTable\":["
                        + "{"
                        + "\"APSLAPSLTY\":\"01\","
                        + "\"APSLAPSLDS\":\"Proposal Type\","
                        + "\"APSLUPDATE\":\"20260817\""
                        + "}"
                        + "]"
                        + "}"
                        + "}";

        mockServer.expect(
                        requestTo(
                                "https://carm-test.company.com"
                                        + "/CarmDataService/referenceTable/data"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(
                        header(
                                "Content-Type",
                                MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(expectedRequest))
                .andRespond(
                        withSuccess(
                                response,
                                MediaType.APPLICATION_JSON));

        CarmReferenceDataResponse result =
                carmClient.fetchReferenceData("1020");

        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(
                1,
                result.getBody()
                        .getReferenceTable()
                        .size());

        assertEquals(
                "01",
                result.getBody()
                        .getReferenceTable()
                        .get(0)
                        .get("APSLAPSLTY"));

        assertEquals(
                "Proposal Type",
                result.getBody()
                        .getReferenceTable()
                        .get(0)
                        .get("APSLAPSLDS"));

        mockServer.verify();
    }

}