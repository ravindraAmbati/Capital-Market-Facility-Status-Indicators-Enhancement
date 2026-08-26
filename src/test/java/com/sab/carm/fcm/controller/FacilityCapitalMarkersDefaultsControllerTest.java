package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersDefaultsResponse;
import com.sab.carm.fcm.service.FacilityCapitalMarkersDefaultsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FacilityCapitalMarkersDefaultsControllerTest {

    @Mock
    private FacilityCapitalMarkersDefaultsService service;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new FacilityCapitalMarkersDefaultsController(service))
                .build();
    }

    @Test
    void shouldReturnDefaults() throws Exception {
        when(service.findAll())
                .thenReturn(new FacilityCapitalMarkersDefaultsResponse());

        mockMvc.perform(get("/api/carm/fcm/defaults")
                        .header(
                                FacilityCapitalMarkersDefaultsController
                                        .CORRELATION_ID_HEADER,
                                "CARM-DEFAULTS-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.correlationId")
                        .value("CARM-DEFAULTS-001"))
                .andExpect(jsonPath("$.header.status")
                        .value("SUCCESS"));
    }

    @Test
    void shouldRequireCorrelationIdHeader() throws Exception {
        mockMvc.perform(get("/api/carm/fcm/defaults"))
                .andExpect(status().isBadRequest());
    }
}
