package com.observatory.observationtracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ObservationTrackerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(ObservationTrackerApplication.class)
                .run(args);
    }
}
