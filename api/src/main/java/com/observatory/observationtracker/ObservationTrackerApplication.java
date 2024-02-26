package com.observatory.observationtracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

// @TODO: Figure out how to mock dev profile
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
