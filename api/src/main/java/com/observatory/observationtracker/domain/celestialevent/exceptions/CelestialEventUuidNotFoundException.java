package com.observatory.observationtracker.domain.celestialevent.exceptions;

public class CelestialEventUuidNotFoundException extends RuntimeException {
    public CelestialEventUuidNotFoundException(String uuid) {
        super("Could not find CelestialEvent with UUID: " + uuid);
    }
}

