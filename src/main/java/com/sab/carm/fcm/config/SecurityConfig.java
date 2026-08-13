package com.sab.carm.fcm.config;

import com.sab.carm.fcm.filter.BearerTokenAuthenticationFilter;
import com.sab.carm.fcm.filter.AuthorizationFilter;
import com.sab.carm.fcm.filter.CorrelationIdFilter;
import com.sab.carm.fcm.filter.RequestLoggingFilter;
import com.sab.carm.fcm.filter.SecurityExceptionFilter;
import com.sab.carm.fcm.security.LdapAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures session and bearer-token security rules.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final LdapAuthenticationProvider authenticationProvider;
    private final BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter;
    private final CorrelationIdFilter correlationIdFilter;
    private final RequestLoggingFilter requestLoggingFilter;
    private final AuthorizationFilter authorizationFilter;
    private final SecurityExceptionFilter securityExceptionFilter;

    public SecurityConfig(LdapAuthenticationProvider authenticationProvider,
            BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter,
            CorrelationIdFilter correlationIdFilter,
            RequestLoggingFilter requestLoggingFilter,
            AuthorizationFilter authorizationFilter,
            SecurityExceptionFilter securityExceptionFilter) {
        this.authenticationProvider = authenticationProvider;
        this.bearerTokenAuthenticationFilter = bearerTokenAuthenticationFilter;
        this.correlationIdFilter = correlationIdFilter;
        this.requestLoggingFilter = requestLoggingFilter;
        this.authorizationFilter = authorizationFilter;
        this.securityExceptionFilter = securityExceptionFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authenticationProvider(authenticationProvider)
                .authorizeRequests()
                .antMatchers("/api/security/authenticate", "/login", "/actuator/health", "/actuator/info").permitAll()
                .antMatchers("/api/security/logout", "/api/security/token", "/api/security/session", "/api/security/profile")
                .hasAnyRole("ADMIN", "API", "READONLY")
                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").hasAnyRole("ADMIN", "READONLY")
                .antMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/read/**").hasAnyRole("ADMIN", "READONLY")
                .antMatchers(HttpMethod.GET, "/api/sample").hasAnyRole("ADMIN", "API", "READONLY")
                .antMatchers(HttpMethod.POST, "/api/sample").hasAnyRole("ADMIN", "API")
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .logout().disable();

        http.addFilterBefore(securityExceptionFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(correlationIdFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(bearerTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(authorizationFilter, BasicAuthenticationFilter.class);
        http.addFilterAfter(requestLoggingFilter, AuthorizationFilter.class);
        return http.build();
    }

    public void configure(AuthenticationManagerBuilder builder) {
        builder.authenticationProvider(authenticationProvider);
    }
}
