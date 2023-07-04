package com.autosale.shop.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> responseStatusExceptionHandler(ResponseStatusException e)
    {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(e.getBody().getDetail());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Void> runtimeExceptionHandler(RuntimeException exception)
    {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }
}
