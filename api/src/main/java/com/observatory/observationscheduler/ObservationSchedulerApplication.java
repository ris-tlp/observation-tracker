package com.observatory.observationscheduler;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ObservationSchedulerApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder()
				.profiles("dev").
				sources(ObservationSchedulerApplication.class)
				.run(args);
	}

}
