package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.dto.FacilityTypeIndicatorRequest;
import com.sab.carm.fcm.dto.FacilityTypeMaintenanceResponse;
import com.sab.carm.fcm.dto.PurposeCodeIndicatorRequest;
import com.sab.carm.fcm.dto.PurposeCodeMaintenanceResponse;
import com.sab.carm.fcm.service.MaintenanceService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    private final MaintenanceService service;

    public MaintenanceController(
            MaintenanceService service) {
        this.service = service;
    }

    @GetMapping("/facility-types")
    public List<FacilityTypeMaintenanceResponse>
    getFacilityTypes() {
        return service.getFacilityTypes();
    }

    @GetMapping("/facility-types/{facilityTypeCode}")
    public FacilityTypeMaintenanceResponse
    getFacilityType(
            @PathVariable String facilityTypeCode) {

        return service.getFacilityType(
                facilityTypeCode);
    }

    @PutMapping("/facility-types/{facilityTypeCode}/indicators")
    public FacilityTypeMaintenanceResponse
    updateFacilityTypeIndicators(
            @PathVariable String facilityTypeCode,
            @Valid @RequestBody
            FacilityTypeIndicatorRequest request) {

        return service.updateFacilityTypeIndicators(
                facilityTypeCode,
                request);
    }

    @GetMapping("/purpose-codes")
    public List<PurposeCodeMaintenanceResponse>
    getPurposeCodes() {
        return service.getPurposeCodes();
    }

    @GetMapping(
            "/purpose-codes/{purposeCodeHub}/{purposeCodeCarm}")
    public PurposeCodeMaintenanceResponse
    getPurposeCode(
            @PathVariable String purposeCodeHub,
            @PathVariable String purposeCodeCarm) {

        return service.getPurposeCode(
                purposeCodeHub,
                purposeCodeCarm);
    }

    @PutMapping(
            "/purpose-codes/{purposeCodeHub}/{purposeCodeCarm}/indicator")
    public PurposeCodeMaintenanceResponse
    updatePurposeCodeIndicator(
            @PathVariable String purposeCodeHub,
            @PathVariable String purposeCodeCarm,
            @Valid @RequestBody
            PurposeCodeIndicatorRequest request) {

        return service.updatePurposeCodeIndicator(
                purposeCodeHub,
                purposeCodeCarm,
                request);
    }
}