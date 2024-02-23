package com.observatory.observationtracker.domain.observation.exceptions;

public class IncorrectObservationFormatException extends RuntimeException {
    public IncorrectObservationFormatException() {
        super("Incorrect Observation JSON format.");
    }
}
