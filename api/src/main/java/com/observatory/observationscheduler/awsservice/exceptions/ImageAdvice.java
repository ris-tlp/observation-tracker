package com.observatory.observationscheduler.awsservice.exceptions;

import com.amazonaws.services.ecr.model.ImageNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ImageAdvice {
    @ResponseBody
    @ExceptionHandler(ImageNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String invalidImageHandler(InvalidImageException ex) {
        return ex.getMessage();
    }

}
