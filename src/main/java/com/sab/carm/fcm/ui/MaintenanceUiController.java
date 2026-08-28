package com.sab.carm.fcm.ui;

import com.sab.carm.fcm.service.MaintenanceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/maintenance")
@PreAuthorize("hasRole('ADMIN')")
public class MaintenanceUiController {

    private final MaintenanceService maintenanceService;

    public MaintenanceUiController(
            MaintenanceService maintenanceService) {

        this.maintenanceService = maintenanceService;
    }

    @GetMapping
    public String maintenance(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String error,
            Model model) {

        model.addAttribute(
                "facilityTypes",
                maintenanceService.getFacilityTypes());

        model.addAttribute(
                "purposeCodes",
                maintenanceService.getPurposeCodes());

        model.addAttribute("type", type);
        model.addAttribute("message", message);
        model.addAttribute("error", error);

        return "maintenance";
    }

    @PostMapping("/facility-types/save")
    public String saveFacilityType(
            @RequestParam String facilityTypeCode,
            @RequestParam String facilityTypeDescription,
            @RequestParam String advised,
            @RequestParam String committed,
            @RequestParam(defaultValue = "false")
            boolean isNew) {

        try {
            maintenanceService.saveFacilityType(
                    facilityTypeCode,
                    facilityTypeDescription,
                    advised,
                    committed,
                    isNew);

            return success(
                    "Facility type saved successfully.");

        } catch (IllegalArgumentException exception) {
            return error(exception.getMessage());
        }
    }

    @PostMapping("/facility-types/delete")
    public String deleteFacilityType(
            @RequestParam String facilityTypeCode) {

        try {
            maintenanceService.deleteFacilityType(
                    facilityTypeCode);

            return success(
                    "Facility type deleted successfully.");

        } catch (IllegalArgumentException exception) {
            return error(exception.getMessage());
        }
    }

    @PostMapping("/purpose-codes/save")
    public String savePurposeCode(
            @RequestParam String purposeCodeHub,
            @RequestParam String purposeCodeCarm,
            @RequestParam String description,
            @RequestParam String unconditionalCancellable,
            @RequestParam(defaultValue = "false")
            boolean isNew) {

        try {
            maintenanceService.savePurposeCode(
                    purposeCodeHub,
                    purposeCodeCarm,
                    description,
                    unconditionalCancellable,
                    isNew);

            return success(
                    "Purpose code saved successfully.");

        } catch (IllegalArgumentException exception) {
            return error(exception.getMessage());
        }
    }

    @PostMapping("/purpose-codes/delete")
    public String deletePurposeCode(
            @RequestParam String purposeCodeHub,
            @RequestParam String purposeCodeCarm) {

        try {
            maintenanceService.deletePurposeCode(
                    purposeCodeHub,
                    purposeCodeCarm);

            return success(
                    "Purpose code deleted successfully.");

        } catch (IllegalArgumentException exception) {
            return error(exception.getMessage());
        }
    }

    private String success(String message) {

        return "redirect:/maintenance?type=SUCCESS&message="
                + encode(message);
    }

    private String error(String message) {

        return "redirect:/maintenance?type=ERROR&error="
                + encode(message);
    }

    private String encode(String value) {

        return URLEncoder.encode(
                value == null
                        ? "Operation failed."
                        : value,
                StandardCharsets.UTF_8);
    }
}
