package com.observatory.observationscheduler.celestialevent.exceptions;

import com.observatory.observationscheduler.celestialevent.CelestialEventStatus;

public class CelestialEventStatusNotFoundException extends RuntimeException {
    public CelestialEventStatusNotFoundException(CelestialEventStatus status) {
        super("Could not find CelestialEvent with status: " + status);
    }
}

