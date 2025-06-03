package com.example.lms.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class HttpStatusException extends RuntimeException {
    private final HttpStatus httpStatus;

    public HttpStatusException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
