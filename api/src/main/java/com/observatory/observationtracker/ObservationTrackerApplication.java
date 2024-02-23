package com.observatory.observationtracker;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ObservationTrackerApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .profiles("dev").
                sources(ObservationTrackerApplication.class)
                .run(args);
    }

}
