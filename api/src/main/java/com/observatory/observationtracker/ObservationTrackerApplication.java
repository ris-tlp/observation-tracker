package com.observatory.observationtracker;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ObservationTrackerApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(ObservationTrackerApplication.class)
                .profiles("dev")
                .run(args);
    }

}
