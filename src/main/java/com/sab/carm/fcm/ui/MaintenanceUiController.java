package com.sab.carm.fcm.ui;

import com.sab.carm.fcm.dto.FacilityTypeIndicatorRequest;
import com.sab.carm.fcm.dto.FacilityTypeMaintenanceResponse;
import com.sab.carm.fcm.dto.PurposeCodeIndicatorRequest;
import com.sab.carm.fcm.dto.PurposeCodeMaintenanceResponse;
import com.sab.carm.fcm.entity.FacilityTypeMaintenance;
import com.sab.carm.fcm.entity.PurposeCodeMaintenance;
import com.sab.carm.fcm.repository.FacilityTypeMaintenanceRepository;
import com.sab.carm.fcm.repository.PurposeCodeMaintenanceRepository;
import com.sab.carm.fcm.service.MaintenanceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/maintenance")
@PreAuthorize("hasRole('ADMIN')")
public class MaintenanceUiController {

    private final MaintenanceService maintenanceService;
    private final FacilityTypeMaintenanceRepository facilityTypeRepository;
    private final PurposeCodeMaintenanceRepository purposeCodeRepository;

    public MaintenanceUiController(
            MaintenanceService maintenanceService,
            FacilityTypeMaintenanceRepository facilityTypeRepository,
            PurposeCodeMaintenanceRepository purposeCodeRepository) {

        this.maintenanceService = maintenanceService;
        this.facilityTypeRepository = facilityTypeRepository;
        this.purposeCodeRepository = purposeCodeRepository;
    }

    @GetMapping
    public String maintenance(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String error,
            Model model) {

        loadModel(model);

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
            @RequestParam(defaultValue = "false") boolean isNew) {

        try {
            validateIndicator(advised);
            validateIndicator(committed);

            if (isNew) {
                if (facilityTypeRepository
                        .findByFacilityTypeCode(facilityTypeCode)
                        .isPresent()) {
                    return redirectError(
                            "Facility type already exists: " + facilityTypeCode);
                }

                FacilityTypeMaintenance entity =
                        new FacilityTypeMaintenance();

                entity.setFacilityTypeCode(facilityTypeCode);
                entity.setFacilityTypeDescription(
                        facilityTypeDescription);
                entity.setAdvised(advised);
                entity.setCommitted(committed);
                entity.setActive(true);

                facilityTypeRepository.save(entity);

                return redirectSuccess(
                        "Facility type added successfully.");
            }

            FacilityTypeMaintenanceResponse current =
                    maintenanceService.getFacilityType(
                            facilityTypeCode);

            FacilityTypeIndicatorRequest request =
                    new FacilityTypeIndicatorRequest();

            request.setAdvised(advised);
            request.setCommitted(committed);

            maintenanceService.updateFacilityTypeIndicators(
                    current.getFacilityTypeCode(),
                    request);

            FacilityTypeMaintenance entity =
                    facilityTypeRepository
                            .findByFacilityTypeCode(facilityTypeCode)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Facility type not found"));

            entity.setFacilityTypeDescription(
                    facilityTypeDescription);

            facilityTypeRepository.save(entity);

            return redirectSuccess(
                    "Facility type updated successfully.");

        } catch (IllegalArgumentException exception) {
            return redirectError(exception.getMessage());
        }
    }

    @PostMapping("/facility-types/delete")
    public String deleteFacilityType(
            @RequestParam String facilityTypeCode) {

        try {
            FacilityTypeMaintenance entity =
                    facilityTypeRepository
                            .findByFacilityTypeCode(facilityTypeCode)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Facility type not found"));

            entity.setActive(false);
            facilityTypeRepository.save(entity);

            return redirectSuccess(
                    "Facility type deleted successfully.");

        } catch (IllegalArgumentException exception) {
            return redirectError(exception.getMessage());
        }
    }

    @PostMapping("/purpose-codes/save")
    public String savePurposeCode(
            @RequestParam String purposeCodeHub,
            @RequestParam String purposeCodeCarm,
            @RequestParam String description,
            @RequestParam String unconditionalCancellable,
            @RequestParam(defaultValue = "false") boolean isNew) {

        try {
            validateIndicator(unconditionalCancellable);

            if (isNew) {
                if (purposeCodeRepository
                        .findByPurposeCodeHubAndPurposeCodeCarm(
                                purposeCodeHub,
                                purposeCodeCarm)
                        .isPresent()) {
                    return redirectError(
                            "Purpose code already exists.");
                }

                PurposeCodeMaintenance entity =
                        new PurposeCodeMaintenance();

                entity.setPurposeCodeHub(purposeCodeHub);
                entity.setPurposeCodeCarm(purposeCodeCarm);
                entity.setDescription(description);
                entity.setUnconditionalCancellable(
                        unconditionalCancellable);
                entity.setActive(true);

                purposeCodeRepository.save(entity);

                return redirectSuccess(
                        "Purpose code added successfully.");
            }

            PurposeCodeIndicatorRequest request =
                    new PurposeCodeIndicatorRequest();

            request.setUnconditionalCancellable(
                    unconditionalCancellable);

            maintenanceService.updatePurposeCodeIndicator(
                    purposeCodeHub,
                    purposeCodeCarm,
                    request);

            PurposeCodeMaintenance entity =
                    purposeCodeRepository
                            .findByPurposeCodeHubAndPurposeCodeCarm(
                                    purposeCodeHub,
                                    purposeCodeCarm)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Purpose code not found"));

            entity.setDescription(description);
            purposeCodeRepository.save(entity);

            return redirectSuccess(
                    "Purpose code updated successfully.");

        } catch (IllegalArgumentException exception) {
            return redirectError(exception.getMessage());
        }
    }

    @PostMapping("/purpose-codes/delete")
    public String deletePurposeCode(
            @RequestParam String purposeCodeHub,
            @RequestParam String purposeCodeCarm) {

        try {
            PurposeCodeMaintenance entity =
                    purposeCodeRepository
                            .findByPurposeCodeHubAndPurposeCodeCarm(
                                    purposeCodeHub,
                                    purposeCodeCarm)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Purpose code not found"));

            entity.setActive(false);
            purposeCodeRepository.save(entity);

            return redirectSuccess(
                    "Purpose code deleted successfully.");

        } catch (IllegalArgumentException exception) {
            return redirectError(exception.getMessage());
        }
    }

    private void loadModel(Model model) {

        List<FacilityTypeMaintenanceResponse> facilityTypes =
                maintenanceService.getFacilityTypes();

        List<PurposeCodeMaintenanceResponse> purposeCodes =
                maintenanceService.getPurposeCodes();

        model.addAttribute("facilityTypes", facilityTypes);
        model.addAttribute("purposeCodes", purposeCodes);
    }

    private void validateIndicator(String value) {
        if (!"Y".equals(value) && !"N".equals(value)) {
            throw new IllegalArgumentException(
                    "Indicator must be Y or N.");
        }
    }

    private String redirectSuccess(String message) {
        return "redirect:/maintenance?type=SUCCESS&message="
                + encode(message);
    }

    private String redirectError(String message) {
        return "redirect:/maintenance?type=ERROR&error="
                + encode(message);
    }

    private String encode(String value) {
        return java.net.URLEncoder.encode(
                value == null ? "Operation failed." : value,
                java.nio.charset.StandardCharsets.UTF_8);
    }
}
