package com.observatory.observationscheduler.celestialevent.exceptions;

import com.observatory.observationscheduler.celestialevent.models.CelestialEventStatus;

public class CelestialEventStatusNotFoundException extends RuntimeException {
    public CelestialEventStatusNotFoundException(CelestialEventStatus status) {
        super("Could not find CelestialEvent with status: " + status);
    }
}

