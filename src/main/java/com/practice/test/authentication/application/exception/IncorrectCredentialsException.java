package com.practice.test.authentication.application.exception;

import com.practice.test.common.exception.DomainException;
import org.springframework.http.HttpStatus;

public class IncorrectCredentialsException extends DomainException {
    public IncorrectCredentialsException() {
        super("exception.incorrect_credentials", null, HttpStatus.BAD_REQUEST.value());
    }
}