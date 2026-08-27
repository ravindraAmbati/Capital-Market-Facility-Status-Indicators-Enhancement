package com.sab.carm.fcm.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String CORRELATION_ID_HEADER =
            "X-CARM-FCM-CorrelationId";

    @Bean
    public OpenAPI carmFcmOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CARM Facility Capital Markers API")
                        .version("1.0.0")
                        .description(
                                "CARM-FCM integration, facility capital markers, "
                                        + "credit-application consent, real-time CSV reporting "
                                        + "and maintenance APIs.")
                        .contact(new Contact()
                                .name("CARM / FCM Application Support")))
                .components(new Components()
                        .addSecuritySchemes(
                                "bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description(
                                                "Bearer token issued by the FCM authentication API.")));
    }

    /**
     * Adds the CARM-FCM integration header only to integration endpoints.
     * UI/maintenance/security endpoints deliberately do not receive this
     * mandatory header in Swagger.
     */
    @Bean
    public OpenApiCustomiser carmFcmOpenApiCustomizer() {

        return openAPI -> {

            if (openAPI.getPaths() == null) {
                return;
            }

            openAPI.getPaths().forEach((path, pathItem) -> {

                boolean integrationPath =
                        path.startsWith("/api/carm/fcm/")
                                || path.startsWith("/api/carm/reference-data/");

                if (!integrationPath) {
                    return;
                }

                pathItem.readOperations().forEach(operation -> {

                    if (operation.getParameters() == null) {
                        operation.setParameters(
                                new java.util.ArrayList<>());
                    }

                    boolean exists =
                            operation.getParameters()
                                    .stream()
                                    .anyMatch(p ->
                                            CORRELATION_ID_HEADER.equals(p.getName())
                                                    && "header".equalsIgnoreCase(p.getIn()));

                    if (!exists) {
                        operation.addParametersItem(
                                new Parameter()
                                        .in("header")
                                        .name(CORRELATION_ID_HEADER)
                                        .description(
                                                "Mandatory CARM-supplied correlation ID used "
                                                        + "to trace the request across CARM, FCM, "
                                                        + "application logs and audit.")
                                        .required(true)
                                        .schema(new io.swagger.v3.oas.models.media.StringSchema()));
                    }

                    operation.addSecurityItem(
                            new SecurityRequirement()
                                    .addList("bearerAuth"));

                    ApiResponses responses = operation.getResponses();

                    if (responses == null) {
                        responses = new ApiResponses();
                        operation.setResponses(responses);
                    }

                    addResponse(responses, "400",
                            "Invalid request, validation failure or missing correlation ID.");
                    addResponse(responses, "401",
                            "Authentication failed or bearer token is missing.");
                    addResponse(responses, "403",
                            "Authenticated user is not authorized.");
                    addResponse(responses, "500",
                            "Unexpected server-side processing error.");
                });
            });
        };
    }

    private void addResponse(
            ApiResponses responses,
            String status,
            String description) {

        if (!responses.containsKey(status)) {
            responses.addApiResponse(
                    status,
                    new ApiResponse().description(description));
        }
    }
}
