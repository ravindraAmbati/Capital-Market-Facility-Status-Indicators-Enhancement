package com.sab.carm.fcm.config;

import com.sab.carm.fcm.filter.AuthorizationFilter;
import com.sab.carm.fcm.filter.BearerTokenAuthenticationFilter;
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

        this.authenticationProvider = authenticationProvider;
        this.bearerTokenAuthenticationFilter =
                bearerTokenAuthenticationFilter;
        this.correlationIdFilter = correlationIdFilter;
        this.requestLoggingFilter = requestLoggingFilter;
        this.authorizationFilter = authorizationFilter;
        this.securityExceptionFilter = securityExceptionFilter;
    }

    @Override
    protected void configure(HttpSecurity http)
            throws Exception {

        http
                .csrf()
                .disable()

                .authenticationProvider(
                        authenticationProvider)

                .authorizeRequests()

                .antMatchers(
                        "/api/security/authenticate",
                        "/login",
                        "/actuator/health",
                        "/actuator/info")
                .permitAll()

                .antMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**")
                .hasAnyRole(
                        "ADMIN",
                        "AUDIT",
                        "ITSUP")

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

                .antMatchers(
                        "/admin/**",
                        "/api/admin/**")
                .hasRole("ADMIN")

                /*
                 * FCM read APIs.
                 * Reports/defaults/facility GET are available to
                 * authenticated service and operational users.
                 */
                .antMatchers(
                        HttpMethod.GET,
                        "/api/carm/fcm/facility",
                        "/api/carm/fcm/defaults",
                        "/api/carm/fcm/report")
                .hasAnyRole(
                        "ADMIN",
                        "API",
                        "AUDIT",
                        "ITSUP")

                /*
                 * FCM CARM integration write APIs.
                 * API is the service-to-service role; ADMIN is
                 * retained for controlled operational use.
                 */
                .antMatchers(
                        HttpMethod.POST,
                        "/api/carm/fcm/facility",
                        "/api/carm/fcm/creditapplication")
                .hasAnyRole(
                        "ADMIN",
                        "API")

                /*
                 * Facility deletion is part of the CARM-FCM
                 * integration lifecycle. API must therefore be
                 * authorized to perform DELETE; it is not limited
                 * to a human ADMIN account.
                 */
                .antMatchers(
                        HttpMethod.DELETE,
                        "/api/carm/fcm/facility")
                .hasAnyRole(
                        "ADMIN",
                        "API")

                /*
                 * Remaining application APIs retain the existing
                 * broad authorization rules.
                 */
                .antMatchers(
                        HttpMethod.GET,
                        "/api/**")
                .hasAnyRole(
                        "ADMIN",
                        "API",
                        "AUDIT",
                        "ITSUP")

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

                .anyRequest()
                .authenticated()

                .and()
                .httpBasic()

                .and()
                .logout()
                .disable();

        http.addFilterBefore(
                securityExceptionFilter,
                UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(
                correlationIdFilter,
                UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(
                bearerTokenAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);

        http.addFilterAfter(
                authorizationFilter,
                BearerTokenAuthenticationFilter.class);

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
