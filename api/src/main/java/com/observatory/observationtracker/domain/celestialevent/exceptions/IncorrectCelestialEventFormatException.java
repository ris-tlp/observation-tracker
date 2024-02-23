package com.observatory.observationtracker.domain.celestialevent.exceptions;

public class IncorrectCelestialEventFormatException extends RuntimeException {
    public IncorrectCelestialEventFormatException() {
        super("Incorrect CelestialEvent JSON format. ");
    }
}
