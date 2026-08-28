package com.sab.carm.fcm.ui;

import com.sab.carm.fcm.ui.testdata.UiMaintenanceDataService;
import com.sab.carm.fcm.ui.testdata.UiTestDataProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/maintenance/test-data")
public class MaintenanceUiTestDataController {

    private final UiMaintenanceDataService testDataService;
    private final UiTestDataProperties properties;

    public MaintenanceUiTestDataController(
            UiMaintenanceDataService testDataService,
            UiTestDataProperties properties) {

        this.testDataService = testDataService;
        this.properties = properties;
    }

    @PostMapping("/reset")
    public String reset() throws IOException {

        if (!properties.isEnabled()) {
            return "redirect:/maintenance";
        }

        testDataService.initialiseTestDataIfEnabled();

        return "redirect:/maintenance?type=SUCCESS&message=Test+data+reset+successfully.";
    }

    @GetMapping("/status")
    @ResponseBody
    public ResponseEntity<String> status() {

        if (!properties.isEnabled()) {
            return ResponseEntity.status(404).body(
                    "{\"enabled\":false}");
        }

        return ResponseEntity.ok(
                "{\"enabled\":true,"
                        + "\"scenario\":\""
                        + properties.getScenario()
                        + "\"}");
    }
}
