package com.company.application.controller;

import com.company.application.audit.AuditService;
import com.company.application.dto.ApiResponse;
import com.company.application.service.SampleService;
import com.company.application.util.SecurityUtil;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sample business endpoints used to validate framework wiring.
 */
@RestController
@RequestMapping("/api")
public class SampleController {

    private final SampleService sampleService;
    private final AuditService auditService;

    public SampleController(SampleService sampleService, AuditService auditService) {
        this.sampleService = sampleService;
        this.auditService = auditService;
    }

    @GetMapping("/sample")
    public ApiResponse readSample() {
        auditService.record("BUSINESS_API_CALL", SecurityUtil.currentUsername(), MDC.get("correlationId"));
        return sampleService.readSample();
    }

    @PostMapping("/sample")
    public ApiResponse createSample() {
        auditService.record("BUSINESS_API_CALL", SecurityUtil.currentUsername(), MDC.get("correlationId"));
        return sampleService.createSample();
    }

    @GetMapping("/admin/sample")
    public ApiResponse adminSample() {
        auditService.record("ADMIN_ACTIVITY", SecurityUtil.currentUsername(), MDC.get("correlationId"));
        return sampleService.readAdminSample();
    }

    @GetMapping("/read/sample")
    public ApiResponse readOnlySample() {
        auditService.record("BUSINESS_API_CALL", SecurityUtil.currentUsername(), MDC.get("correlationId"));
        return sampleService.readOnlySample();
    }
}
