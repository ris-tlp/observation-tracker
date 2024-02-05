package com.observatory.observationscheduler.useraccount.exceptions;

public class IncorrectUserFormatException extends RuntimeException {
    public IncorrectUserFormatException() {
        super("User JSON format is incorrect.");
    }
}
