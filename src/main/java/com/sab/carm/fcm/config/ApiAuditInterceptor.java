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

    public static final String TRANSACTION_ID_ATTRIBUTE =
            "CARM_FCM_TRANSACTION_ID";

    public static final String RELATIONSHIP_ID_ATTRIBUTE =
            "CARM_FCM_RELATIONSHIP_ID";

    public static final String SERIAL_NO_ATTRIBUTE =
            "CARM_FCM_SERIAL_NO";

    public static final String FACILITY_NO_ATTRIBUTE =
            "CARM_FCM_FACILITY_NO";

    public static final String USER_ID_ATTRIBUTE =
            "CARM_FCM_USER_ID";

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

        String transactionId =
                CarmFcmTransactionContext.getTransactionId();

        if (transactionId == null
                || transactionId.trim().isEmpty()) {
            Object attribute =
                    request.getAttribute(
                            TRANSACTION_ID_ATTRIBUTE);

            if (attribute != null) {
                transactionId = attribute.toString();
            }
        }

        auditService.audit(
                correlationId,
                transactionId,
                request.getMethod(),
                request.getRequestURI(),
                resolveOperation(
                        request.getMethod(),
                        request.getRequestURI()),
                response.getStatus() >= 400
                        ? "FAILED"
                        : "SUCCESS",
                value(
                        request,
                        RELATIONSHIP_ID_ATTRIBUTE,
                        "relationshipId"),
                value(
                        request,
                        SERIAL_NO_ATTRIBUTE,
                        "serialNo"),
                value(
                        request,
                        FACILITY_NO_ATTRIBUTE,
                        "facilityNo"),
                value(
                        request,
                        USER_ID_ATTRIBUTE,
                        "userId"),
                null);
    }

    private String value(
            HttpServletRequest request,
            String attributeName,
            String parameterName) {

        Object attribute =
                request.getAttribute(attributeName);

        if (attribute != null) {
            return attribute.toString();
        }

        return request.getParameter(parameterName);
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
