package com.practice.test.common.exception;

import org.springframework.http.HttpStatus;

public class IncorrectCredentialsException extends GeneralException {
    public IncorrectCredentialsException() {
        super("exception.incorrect_credentials", null, HttpStatus.UNAUTHORIZED);
    }
}