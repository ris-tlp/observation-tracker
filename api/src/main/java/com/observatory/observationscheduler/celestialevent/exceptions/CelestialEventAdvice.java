package com.observatory.observationscheduler.celestialevent.exceptions;

import com.observatory.observationscheduler.observation.exceptions.IncorrectObservationFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CelestialEventAdvice {
    @ResponseBody
    @ExceptionHandler(CelestialEventUuidNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String celestialEventNotFoundHandler(CelestialEventUuidNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(CelestialEventStatusNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String celestialEventNotFoundHandler(CelestialEventStatusNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(IncorrectCelestialEventFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String incorrectCelestialEventFormatHandler(IncorrectObservationFormatException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(CelestialEventCommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String celestialEventCommentNotFoundHandler(CelestialEventCommentNotFoundException ex) {
        return ex.getMessage();
    }
}
