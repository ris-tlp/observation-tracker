package com.observatory.observationscheduler.observation;

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
    @ExceptionHandler(IncorrectObservationFormat.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String incorrectObservationFormatHandler(IncorrectObservationFormat ex) { return ex.getMessage(); }
}


class ObservationNotFoundException extends RuntimeException{
    ObservationNotFoundException(String uuid) {
        super("Could not find Observation with UUID: " + uuid);
    }
}

class IncorrectObservationFormat extends RuntimeException {
    IncorrectObservationFormat() {  super("Incorrect Observation JSON format."); }
}