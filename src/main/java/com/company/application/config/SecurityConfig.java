package com.company.application.config;

import com.company.application.filter.BearerTokenAuthenticationFilter;
import com.company.application.filter.CorrelationIdFilter;
import com.company.application.filter.RequestLoggingFilter;
import com.company.application.security.LdapAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
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

    public SecurityConfig(LdapAuthenticationProvider authenticationProvider,
            BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter,
            CorrelationIdFilter correlationIdFilter,
            RequestLoggingFilter requestLoggingFilter) {
        this.authenticationProvider = authenticationProvider;
        this.bearerTokenAuthenticationFilter = bearerTokenAuthenticationFilter;
        this.correlationIdFilter = correlationIdFilter;
        this.requestLoggingFilter = requestLoggingFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authenticationProvider(authenticationProvider)
                .authorizeRequests()
                .antMatchers("/api/security/authenticate", "/actuator/health", "/actuator/info").permitAll()
                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").hasAnyRole("ADMIN", "READONLY")
                .antMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/read/**").hasAnyRole("ADMIN", "READONLY")
                .antMatchers(HttpMethod.GET, "/api/sample").hasAnyRole("ADMIN", "API", "READONLY")
                .antMatchers(HttpMethod.POST, "/api/sample").hasAnyRole("ADMIN", "API")
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .logout().logoutUrl("/logout").invalidateHttpSession(true).clearAuthentication(true);

        http.addFilterBefore(correlationIdFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(bearerTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(requestLoggingFilter, BearerTokenAuthenticationFilter.class);
        return http.build();
    }

    public void configure(AuthenticationManagerBuilder builder) {
        builder.authenticationProvider(authenticationProvider);
    }
}
