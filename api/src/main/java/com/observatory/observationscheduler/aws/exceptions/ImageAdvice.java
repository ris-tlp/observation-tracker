package com.observatory.observationscheduler.aws.exceptions;

import com.amazonaws.services.ecr.model.ImageNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@ControllerAdvice
public class ImageAdvice {
    @ResponseBody
    @ExceptionHandler({ImageNotFoundException.class, IOException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String invalidImageHandler(InvalidImageException ex) {
        return ex.getMessage();
    }

}
