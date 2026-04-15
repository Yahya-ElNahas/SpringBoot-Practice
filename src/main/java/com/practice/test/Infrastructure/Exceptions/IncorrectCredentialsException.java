package com.practice.test.Infrastructure.Exceptions;

import org.springframework.http.HttpStatus;

public class IncorrectCredentialsException extends CodedException {
    public IncorrectCredentialsException() {
        super("Incorrect email or password", HttpStatus.UNAUTHORIZED);
    }
}
