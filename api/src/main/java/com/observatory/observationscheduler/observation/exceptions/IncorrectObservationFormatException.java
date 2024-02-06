package com.observatory.observationscheduler.observation.exceptions;

public class IncorrectObservationFormatException extends RuntimeException {
    public IncorrectObservationFormatException() {
        super("Incorrect Observation JSON format.");
    }
}
