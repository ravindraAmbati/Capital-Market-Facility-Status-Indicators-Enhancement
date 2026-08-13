package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.dto.ApiResponse;
import com.sab.carm.fcm.dto.AuthenticationRequest;
import com.sab.carm.fcm.service.SecurityService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Session login and logout endpoints for Admin and ReadOnly users.
 */
@RestController
public class SessionSecurityController {

    private final SecurityService securityService;

    public SessionSecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody AuthenticationRequest request, HttpServletRequest servletRequest) {
        securityService.login(request, servletRequest);
        return new ApiResponse("login successful");
    }

    @PostMapping("/logout")
    public ApiResponse logout(HttpServletRequest servletRequest) {
        securityService.logout(servletRequest);
        return new ApiResponse("logout successful");
    }
}
