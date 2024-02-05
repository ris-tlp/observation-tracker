package com.observatory.observationscheduler.observation.exceptions;

public class ObservationNotFoundException extends RuntimeException {
    public ObservationNotFoundException(String uuid) {
        super("Could not find Observation with UUID: " + uuid);
    }
}
