package com.practice.main.authentication.application.exception;

import com.practice.main.common.exception.DomainException;
import org.springframework.http.HttpStatus;

public class IncorrectCredentialsException extends DomainException {
    public IncorrectCredentialsException() {
        super("exception.incorrect_credentials", null, "AUTH", "003", "INCORRECT_CREDENTIALS");
    }
}