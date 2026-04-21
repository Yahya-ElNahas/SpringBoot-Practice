package com.practice.test.Infrastructure.Exceptions;

import org.springframework.http.HttpStatus;

public class InvalidSessionException extends GeneralException {
    public InvalidSessionException() {
        super("exception.invalid_session", null, HttpStatus.UNAUTHORIZED);
    }
}
