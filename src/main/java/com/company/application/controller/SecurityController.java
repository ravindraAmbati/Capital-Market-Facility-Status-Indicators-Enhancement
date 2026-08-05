package com.company.application.controller;

import com.company.application.dto.AuthenticationRequest;
import com.company.application.dto.AuthenticationResponse;
import com.company.application.dto.SecurityProfileResponse;
import com.company.application.dto.SessionStatusResponse;
import com.company.application.dto.TokenStatusResponse;
import com.company.application.dto.ApiResponse;
import com.company.application.service.SecurityService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API authentication endpoints.
 */
@RestController
@RequestMapping("/api/security")
public class SecurityController {

    private final SecurityService securityService;

    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request, HttpServletRequest servletRequest) {
        return securityService.authenticate(request, servletRequest);
    }

    @PostMapping("/logout")
    public ApiResponse logout(HttpServletRequest request) {
        securityService.logout(request);
        return new ApiResponse("logout successful");
    }

    @GetMapping("/token")
    public TokenStatusResponse token(HttpServletRequest request) {
        return securityService.tokenStatus(request);
    }

    @GetMapping("/session")
    public SessionStatusResponse session(HttpServletRequest request) {
        return securityService.sessionStatus(request);
    }

    @GetMapping("/profile")
    public SecurityProfileResponse profile(HttpServletRequest request) {
        return securityService.profile(request);
    }
}
