package com.company.application.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Serves Thymeleaf admin console placeholder pages.
 */
@Controller
public class AdminConsoleController {

    @GetMapping({"/admin", "/system", "/configuration", "/tokens", "/audit"})
    public String console() {
        return "console";
    }

    @GetMapping("/health")
    public String health() {
        return "console";
    }
}
