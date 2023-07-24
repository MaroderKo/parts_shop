package com.autosale.shop.model;

import lombok.Value;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Value
public class ExceptionResponse {
    Timestamp timestamp;
    int status;
    HttpStatus error;
    String message;

    public ExceptionResponse(HttpStatus error, String message) {
        this.timestamp = Timestamp.valueOf(LocalDateTime.now());
        this.status = error.value();
        this.error = error;
        this.message = message;
    }
}
