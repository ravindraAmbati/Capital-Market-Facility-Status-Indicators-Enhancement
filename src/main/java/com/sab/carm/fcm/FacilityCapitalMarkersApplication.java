package com.sab.carm.fcm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Starts the enterprise foundation application.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class FacilityCapitalMarkersApplication {

    public static void main(String[] args) {
        SpringApplication.run(FacilityCapitalMarkersApplication.class, args);
    }
}
