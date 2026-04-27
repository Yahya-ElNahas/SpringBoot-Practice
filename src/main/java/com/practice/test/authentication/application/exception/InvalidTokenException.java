package com.practice.test.authentication.application.exception;

import com.practice.test.common.exception.DomainException;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends DomainException {
    public InvalidTokenException() {
        super("exception.invalid_token", null, HttpStatus.UNAUTHORIZED.value());
    }
}
