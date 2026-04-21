package com.practice.test.Infrastructure.Exceptions;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends GeneralException {
    public InvalidTokenException() {
        super("exception.invalid_token", null, HttpStatus.UNAUTHORIZED);
    }
}
