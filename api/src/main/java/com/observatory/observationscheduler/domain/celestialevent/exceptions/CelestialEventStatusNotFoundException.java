package com.observatory.observationscheduler.domain.celestialevent.exceptions;

import com.observatory.observationscheduler.domain.celestialevent.models.CelestialEventStatus;

public class CelestialEventStatusNotFoundException extends RuntimeException {
    public CelestialEventStatusNotFoundException(CelestialEventStatus status) {
        super("Could not find CelestialEvent with status: " + status);
    }
}

