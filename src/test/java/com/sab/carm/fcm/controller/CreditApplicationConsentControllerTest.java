package com.sab.carm.fcm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sab.carm.fcm.dto.integration.CreditApplicationConsentRequest;
import com.sab.carm.fcm.dto.integration.CreditApplicationConsentResponse;
import com.sab.carm.fcm.dto.integration.DecisionType;
import com.sab.carm.fcm.service.CreditApplicationConsentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CreditApplicationConsentControllerTest {

    @Mock
    private CreditApplicationConsentService service;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new CreditApplicationConsentController(service))
                .build();
    }

    @Test
    void shouldPostConsent() throws Exception {
        CreditApplicationConsentResponse response =
                new CreditApplicationConsentResponse();

        response.setRelationshipId("REL001");
        response.setSerialNo("001");

        when(service.addConsent(
                any(CreditApplicationConsentRequest.class),
                eq("CARM-CORR-001")))
                .thenReturn(response);

        mockMvc.perform(post(
                "/api/carm/fcm/creditapplication")
                .header(
                        CreditApplicationConsentController
                                .CORRELATION_ID_HEADER,
                        "CARM-CORR-001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request())))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.header.correlationId")
                        .value("CARM-CORR-001"))
                .andExpect(jsonPath(
                        "$.header.status")
                        .value("SUCCESS"))
                .andExpect(jsonPath(
                        "$.body.relationshipId")
                        .value("REL001"))
                .andExpect(jsonPath(
                        "$.body.serialNo")
                        .value("001"));
    }

    @Test
    void shouldRequireCorrelationId() throws Exception {
        mockMvc.perform(post(
                "/api/carm/fcm/creditapplication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request())))
                .andExpect(status().isBadRequest());
    }

    private CreditApplicationConsentRequest request() {
        CreditApplicationConsentRequest request =
                new CreditApplicationConsentRequest();

        request.setRelationshipId("REL001");
        request.setSerialNo("001");
        request.setDecision(DecisionType.RECOMMEND);
        request.setHubUserId("AB12");

        return request;
    }
}
