package com.practice.test.authentication.application.exception;

import com.practice.test.common.exception.DomainException;
import org.springframework.http.HttpStatus;

public class InvalidSessionException extends DomainException {
    public InvalidSessionException() {
        super("exception.invalid_session", null, HttpStatus.UNAUTHORIZED.value());
    }
}
