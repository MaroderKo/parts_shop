package com.autosale.shop.exception.handler;

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
}
