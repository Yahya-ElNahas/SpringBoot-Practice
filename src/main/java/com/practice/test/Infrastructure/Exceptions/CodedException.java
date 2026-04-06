package com.practice.test.Infrastructure.Exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CodedException extends RuntimeException {

    private final HttpStatus status;

    public CodedException(String message, final HttpStatus status) {
        super(message);
        this.status = status;
    }
}
