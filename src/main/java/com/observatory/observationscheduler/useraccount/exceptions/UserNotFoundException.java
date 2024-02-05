package com.observatory.observationscheduler.useraccount.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String uuid) {
        super("Could not find User with UUID: " + uuid);
    }
}
