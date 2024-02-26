package com.observatory.observationtracker;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

// @TODO: Figure out profiles otherwise it's too dirty
// @TODO: Dockerfile
@SpringBootApplication
public class ObservationTrackerApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(ObservationTrackerApplication.class)
//                .profiles("dev")
                .run(args);
    }

}
