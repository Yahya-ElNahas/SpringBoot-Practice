package com.practice.test.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidSessionException extends GeneralException {
    public InvalidSessionException() {
        super("exception.invalid_session", null, HttpStatus.UNAUTHORIZED);
    }
}
