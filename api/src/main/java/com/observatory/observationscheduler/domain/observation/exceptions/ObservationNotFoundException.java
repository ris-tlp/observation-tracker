package com.observatory.observationscheduler.domain.observation.exceptions;

public class ObservationNotFoundException extends RuntimeException {
    public ObservationNotFoundException(String uuid) {
        super("Could not find Observation with UUID: " + uuid);
    }
}
