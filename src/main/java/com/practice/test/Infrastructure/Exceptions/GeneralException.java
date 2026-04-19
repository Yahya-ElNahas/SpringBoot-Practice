package com.practice.test.Infrastructure.Exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GeneralException extends RuntimeException {

    private final HttpStatus status;

    public GeneralException(String message, final HttpStatus status) {
        super(message);
        this.status = status;
    }
}
