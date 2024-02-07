package com.observatory.observationscheduler.awsservice.exceptions;

import com.observatory.observationscheduler.celestialevent.CelestialEventStatus;

import java.io.IOException;

public class InvalidImageException extends IOException {
    public InvalidImageException() {
        super("There was an issue with the image uploaded. Please try again");
    }
}



