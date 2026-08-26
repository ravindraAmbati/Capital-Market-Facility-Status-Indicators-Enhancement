package com.sab.carm.fcm.config;

import com.sab.carm.fcm.service.ApiAuditService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ApiAuditInterceptor implements HandlerInterceptor {

    public static final String CORRELATION_ID_HEADER =
            "X-CARM-FCM-CorrelationId";

    private final ApiAuditService auditService;

    public ApiAuditInterceptor(ApiAuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception exception) {

        if (!request.getRequestURI().startsWith("/api/")) {
            return;
        }

        String correlationId =
                request.getHeader(CORRELATION_ID_HEADER);

        if (correlationId == null
                || correlationId.trim().isEmpty()) {
            return;
        }

        auditService.audit(
                correlationId,
                request.getMethod(),
                request.getRequestURI(),
                resolveOperation(
                        request.getMethod(),
                        request.getRequestURI()),
                response.getStatus() >= 400
                        ? "FAILED"
                        : "SUCCESS",
                request.getParameter("relationshipId"),
                request.getParameter("serialNo"),
                request.getParameter("facilityNo"),
                null,
                null);
    }

    private String resolveOperation(
            String method,
            String path) {

        if ("/api/carm/fcm/facility".equals(path)) {
            if ("GET".equalsIgnoreCase(method)) {
                return "FACILITY_GET";
            }
            if ("POST".equalsIgnoreCase(method)) {
                return "FACILITY_UPSERT";
            }
            if ("DELETE".equalsIgnoreCase(method)) {
                return "FACILITY_DELETE";
            }
        }

        if ("/api/carm/fcm/defaults".equals(path)
                && "GET".equalsIgnoreCase(method)) {
            return "DEFAULTS_GET";
        }

        if ("/api/carm/fcm/creditapplication".equals(path)
                && "POST".equalsIgnoreCase(method)) {
            return "CREDIT_APPLICATION_CONSENT";
        }

        if ("/api/carm/fcm/report".equals(path)
                && "GET".equalsIgnoreCase(method)) {
            return "REPORT_GET";
        }

        return "UNKNOWN_API_OPERATION";
    }
}
