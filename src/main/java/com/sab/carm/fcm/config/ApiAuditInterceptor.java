package com.sab.carm.fcm.config;

import com.sab.carm.fcm.service.ApiAuditService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

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

        if (request.getRequestURI().startsWith("/api/")) {
            String correlationId =
                    request.getHeader(CORRELATION_ID_HEADER);

            if (correlationId != null && !correlationId.trim().isEmpty()) {
                auditService.audit(
                        correlationId,
                        request.getMethod(),
                        request.getRequestURI(),
                        request.getMethod(),
                        response.getStatus() >= 400
                                ? "FAILED"
                                : "SUCCESS",
                        request.getParameter("relationshipId"),
                        request.getParameter("serialNo"),
                        request.getParameter("facilityNo"),
                        null,
                        Collections.emptyMap());
            }
        }
    }
}
