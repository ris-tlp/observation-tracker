package com.observatory.observationtracker.domain.useraccount.exceptions;

public class IncorrectUserFormatException extends RuntimeException {
    public IncorrectUserFormatException() {
        super("User JSON format is incorrect.");
    }
}
