package com.sab.carm.fcm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersOperationResponse;
import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersRequest;
import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersResponse;
import com.sab.carm.fcm.dto.integration.FacilityOperation;
import com.sab.carm.fcm.service.FacilityCapitalMarkersService;
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
class FacilityCapitalMarkersPostControllerTest {

    @Mock
    private FacilityCapitalMarkersService service;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new FacilityCapitalMarkersController(service))
                .build();
    }

    @Test
    void shouldReturnCreatedOperation() throws Exception {
        when(service.upsert(any(FacilityCapitalMarkersRequest.class),
                eq("CARM-001")))
                .thenReturn(operation(FacilityOperation.CREATED));

        mockMvc.perform(post("/api/carm/fcm/facility")
                        .header(
                                FacilityCapitalMarkersController
                                        .CORRELATION_ID_HEADER,
                                "CARM-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request("Y"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.correlationId")
                        .value("CARM-001"))
                .andExpect(jsonPath("$.body.operation")
                        .value("CREATED"));
    }

    @Test
    void shouldReturnNoChangeOperation() throws Exception {
        when(service.upsert(any(FacilityCapitalMarkersRequest.class),
                eq("CARM-001")))
                .thenReturn(operation(FacilityOperation.NO_CHANGE));

        mockMvc.perform(post("/api/carm/fcm/facility")
                        .header(
                                FacilityCapitalMarkersController
                                        .CORRELATION_ID_HEADER,
                                "CARM-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request("Y"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.operation")
                        .value("NO_CHANGE"));
    }

    private FacilityCapitalMarkersRequest request(String indicator) {
        FacilityCapitalMarkersRequest request =
                new FacilityCapitalMarkersRequest();

        request.setCreditApplicationRelationshipId("REL001");
        request.setSerialNo("001");
        request.setFacilityNo("123");
        request.setFacilityType("FT01");
        request.setCarmPurposeCode("PURP01");
        request.setAdvised(marker(indicator));
        request.setCommitted(marker("Y"));
        request.setUnconditionalCancellable(marker("Y"));

        return request;
    }

    private FacilityCapitalMarkersRequest.CapitalMarkerRequest marker(
            String indicator) {

        FacilityCapitalMarkersRequest.CapitalMarkerRequest marker =
                new FacilityCapitalMarkersRequest.CapitalMarkerRequest();

        marker.setIndicator(indicator);
        return marker;
    }

    private FacilityCapitalMarkersOperationResponse operation(
            FacilityOperation operation) {

        FacilityCapitalMarkersOperationResponse response =
                new FacilityCapitalMarkersOperationResponse();

        response.setOperation(operation);
        response.setFacilityCapitalMarkers(
                new FacilityCapitalMarkersResponse());

        return response;
    }
}
