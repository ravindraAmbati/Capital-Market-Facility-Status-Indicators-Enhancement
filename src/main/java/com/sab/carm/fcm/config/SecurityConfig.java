package com.sab.carm.fcm.config;

import com.sab.carm.fcm.filter.AuthorizationFilter;
import com.sab.carm.fcm.filter.BearerTokenAuthenticationFilter;
import com.sab.carm.fcm.filter.CorrelationIdFilter;
import com.sab.carm.fcm.filter.RequestLoggingFilter;
import com.sab.carm.fcm.filter.SecurityExceptionFilter;
import com.sab.carm.fcm.security.LdapAuthenticationProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures authentication and authorization for the application.
 *
 * Authentication:
 * - LDAP authentication provider
 * - Bearer token authentication
 *
 * Authorization:
 * - ADMIN
 * - API
 * - AUDIT
 * - ITSUP
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig
        extends WebSecurityConfigurerAdapter {

    private final LdapAuthenticationProvider authenticationProvider;
    private final BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter;
    private final CorrelationIdFilter correlationIdFilter;
    private final RequestLoggingFilter requestLoggingFilter;
    private final AuthorizationFilter authorizationFilter;
    private final SecurityExceptionFilter securityExceptionFilter;

    public SecurityConfig(
            LdapAuthenticationProvider authenticationProvider,
            BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter,
            CorrelationIdFilter correlationIdFilter,
            RequestLoggingFilter requestLoggingFilter,
            AuthorizationFilter authorizationFilter,
            SecurityExceptionFilter securityExceptionFilter) {

        this.authenticationProvider =
                authenticationProvider;

        this.bearerTokenAuthenticationFilter =
                bearerTokenAuthenticationFilter;

        this.correlationIdFilter =
                correlationIdFilter;

        this.requestLoggingFilter =
                requestLoggingFilter;

        this.authorizationFilter =
                authorizationFilter;

        this.securityExceptionFilter =
                securityExceptionFilter;
    }

    @Override
    protected void configure(
            HttpSecurity http)
            throws Exception {

        http
                .csrf()
                .disable()

                .authenticationProvider(
                        authenticationProvider)

                .authorizeRequests()

                /*
                 * Authentication endpoints.
                 */
                .antMatchers(
                        "/api/security/authenticate",
                        "/login",
                        "/actuator/health",
                        "/actuator/info")
                .permitAll()

                /*
                 * Swagger/OpenAPI.
                 *
                 * Swagger is available only to users
                 * authenticated with ADMIN, AUDIT or ITSUP.
                 *
                 * API service users do not need Swagger access.
                 */
                .antMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**")
                .hasAnyRole(
                        "ADMIN",
                        "AUDIT",
                        "ITSUP")

                /*
                 * Security session/profile endpoints.
                 */
                .antMatchers(
                        "/api/security/logout",
                        "/api/security/token",
                        "/api/security/session",
                        "/api/security/profile")
                .hasAnyRole(
                        "ADMIN",
                        "API",
                        "AUDIT",
                        "ITSUP")

                /*
                 * Administrative APIs.
                 */
                .antMatchers(
                        "/admin/**",
                        "/api/admin/**")
                .hasRole("ADMIN")

                /*
                 * Read-only application APIs.
                 *
                 * AUDIT and ITSUP are allowed to read.
                 * API users are also allowed to read.
                 */
                .antMatchers(
                        HttpMethod.GET,
                        "/api/**")
                .hasAnyRole(
                        "ADMIN",
                        "API",
                        "AUDIT",
                        "ITSUP")

                /*
                 * Write APIs are restricted to ADMIN/API.
                 *
                 * This is intentionally broad for the current
                 * framework stage. Individual business APIs
                 * can introduce more restrictive rules later.
                 */
                .antMatchers(
                        HttpMethod.POST,
                        "/api/**")
                .hasAnyRole(
                        "ADMIN",
                        "API")

                .antMatchers(
                        HttpMethod.PUT,
                        "/api/**")
                .hasAnyRole(
                        "ADMIN",
                        "API")

                .antMatchers(
                        HttpMethod.DELETE,
                        "/api/**")
                .hasRole("ADMIN")
                .antMatchers(
                        HttpMethod.POST,
                        "/api/carm/reference-data/refresh/**")
                .hasRole("ADMIN")

                .antMatchers(
                        HttpMethod.PUT,
                        "/api/maintenance/facility-types/*/indicators",
                        "/api/maintenance/purpose-codes/*/*/indicator")
                .hasRole("ADMIN")

                .antMatchers(
                        HttpMethod.GET,
                        "/api/maintenance/facility-types",
                        "/api/maintenance/facility-types/*",
                        "/api/maintenance/purpose-codes",
                        "/api/maintenance/purpose-codes/*/*")
                .hasAnyRole(
                        "ADMIN",
                        "API",
                        "READONLY",
                        "ITSUP",
                        "AUDIT")
                /*
                 * Everything else requires authentication.
                 */
                .anyRequest()
                .authenticated()

                .and()
                .httpBasic()

                .and()
                .logout()
                .disable();

        /*
         * Exception handling must be early enough to
         * capture security exceptions generated by
         * downstream filters.
         */
        http.addFilterBefore(
                securityExceptionFilter,
                UsernamePasswordAuthenticationFilter.class);

        /*
         * Correlation ID should be available to all
         * subsequent security and application processing.
         */
        http.addFilterBefore(
                correlationIdFilter,
                UsernamePasswordAuthenticationFilter.class);

        /*
         * Bearer token must be processed before the
         * authorization filter.
         */
        http.addFilterBefore(
                bearerTokenAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);

        /*
         * Authorization must run after bearer-token
         * authentication has populated SecurityContext.
         */
        http.addFilterAfter(
                authorizationFilter,
                BearerTokenAuthenticationFilter.class);

        /*
         * Request logging should happen after security
         * processing so that the authenticated username
         * is available.
         */
        http.addFilterAfter(
                requestLoggingFilter,
                AuthorizationFilter.class);
    }

    @Override
    protected void configure(
            AuthenticationManagerBuilder auth)
            throws Exception {

        auth.authenticationProvider(
                authenticationProvider);
    }
}