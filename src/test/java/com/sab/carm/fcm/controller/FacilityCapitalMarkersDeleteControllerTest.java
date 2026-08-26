package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.service.FacilityCapitalMarkersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FacilityCapitalMarkersDeleteControllerTest {

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
    void shouldReturn200WhenFacilityIsDeleted() throws Exception {
        when(service.delete(
                "REL001",
                "001",
                "123",
                "CARM-DELETE-001"))
                .thenReturn(true);

        mockMvc.perform(delete("/api/carm/fcm/facility")
                        .header(
                                FacilityCapitalMarkersController
                                        .CORRELATION_ID_HEADER,
                                "CARM-DELETE-001")
                        .param("relationshipId", "REL001")
                        .param("serialNo", "001")
                        .param("facilityNo", "123"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenFacilityDoesNotExist() throws Exception {
        when(service.delete(
                "REL001",
                "001",
                "123",
                "CARM-DELETE-001"))
                .thenReturn(false);

        mockMvc.perform(delete("/api/carm/fcm/facility")
                        .header(
                                FacilityCapitalMarkersController
                                        .CORRELATION_ID_HEADER,
                                "CARM-DELETE-001")
                        .param("relationshipId", "REL001")
                        .param("serialNo", "001")
                        .param("facilityNo", "123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRequireCorrelationIdHeader() throws Exception {
        mockMvc.perform(delete("/api/carm/fcm/facility")
                        .param("relationshipId", "REL001")
                        .param("serialNo", "001")
                        .param("facilityNo", "123"))
                .andExpect(status().isBadRequest());
    }
}
