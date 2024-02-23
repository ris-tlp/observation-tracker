package com.observatory.observationtracker.domain.observation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class ObservationAdvice {
    @ResponseBody
    @ExceptionHandler(ObservationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String observationNotFoundHandler(ObservationNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(IncorrectObservationFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String incorrectObservationFormatHandler(IncorrectObservationFormatException ex) {
        return ex.getMessage();
    }
}


