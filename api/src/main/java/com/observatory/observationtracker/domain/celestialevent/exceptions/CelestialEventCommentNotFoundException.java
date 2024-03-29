package com.observatory.observationtracker.domain.celestialevent.exceptions;

public class CelestialEventCommentNotFoundException extends RuntimeException {
    public CelestialEventCommentNotFoundException(String uuid) {
        super("Could not find CelestialEventComment with UUID: " + uuid);
    }
}
