package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersRequest;
import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersResponse;
import com.sab.carm.fcm.service.FacilityCapitalMarkersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FacilityCapitalMarkersControllerTest {

    @Mock
    private FacilityCapitalMarkersService service;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new FacilityCapitalMarkersController(service))
                .build();
    }

    @Test
    void shouldReturnFacilityWhenRecordExists() throws Exception {
        FacilityCapitalMarkersResponse response =
                new FacilityCapitalMarkersResponse();

        response.setCreditApplicationRelationshipId("REL001");
        response.setSerialNo("001");
        response.setFacilityNo("123");
        response.setFacilityType("FT01");
        response.setCarmPurposeCode("PURP01");

        FacilityCapitalMarkersRequest.CapitalMarkerRequest advised =
                new FacilityCapitalMarkersRequest.CapitalMarkerRequest();
        advised.setIndicator("Y");
        response.setAdvised(advised);

        when(service.find("REL001", "001", "123"))
                .thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/carm/fcm/facility")
                        .header(
                                FacilityCapitalMarkersController
                                        .CORRELATION_ID_HEADER,
                                "CARM-001")
                        .param("relationshipId", "REL001")
                        .param("serialNo", "001")
                        .param("facilityNo", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.header.correlationId",
                        is("CARM-001")))
                .andExpect(jsonPath(
                        "$.header.status",
                        is("SUCCESS")))
                .andExpect(jsonPath(
                        "$.body.facilityNo",
                        is("123")))
                .andExpect(jsonPath(
                        "$.body.facilityType",
                        is("FT01")));
    }

    @Test
    void shouldReturn404WhenRecordDoesNotExist() throws Exception {
        when(service.find("REL001", "001", "123"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/carm/fcm/facility")
                        .header(
                                FacilityCapitalMarkersController
                                        .CORRELATION_ID_HEADER,
                                "CARM-001")
                        .param("relationshipId", "REL001")
                        .param("serialNo", "001")
                        .param("facilityNo", "123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRequireCorrelationIdHeader() throws Exception {
        mockMvc.perform(get("/api/carm/fcm/facility")
                        .param("relationshipId", "REL001")
                        .param("serialNo", "001")
                        .param("facilityNo", "123"))
                .andExpect(status().isBadRequest());
    }
}
