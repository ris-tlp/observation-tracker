package com.observatory.observationscheduler.celestialevent.exceptions;

public class IncorrectCelestialEventFormatException extends RuntimeException{
    public IncorrectCelestialEventFormatException() { super("Incorrect CelestialEvent JSON format. ");}
}
