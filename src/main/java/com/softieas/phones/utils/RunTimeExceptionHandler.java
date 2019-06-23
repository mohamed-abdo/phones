package com.softieas.phones.utils;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Order
@ControllerAdvice
public class RunTimeExceptionHandler extends RuntimeException {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> genericHandler(Exception ex) {
        return new ApiError(INTERNAL_SERVER_ERROR, ex.getMessage(), ex)
                .buildResponseEntity();
    }
}
