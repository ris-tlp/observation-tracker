package com.observatory.observationscheduler.domain.useraccount.exceptions;

public class IncorrectUserFormatException extends RuntimeException {
    public IncorrectUserFormatException() {
        super("User JSON format is incorrect.");
    }
}
