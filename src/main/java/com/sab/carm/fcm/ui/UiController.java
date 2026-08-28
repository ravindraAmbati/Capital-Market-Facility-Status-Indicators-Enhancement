package com.sab.carm.fcm.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {

    @GetMapping("/")
    public String home() {
        return "redirect:/maintenance";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "403";
    }
}
