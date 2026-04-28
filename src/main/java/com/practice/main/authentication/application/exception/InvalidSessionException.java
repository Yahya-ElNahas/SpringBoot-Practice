package com.practice.main.authentication.application.exception;

import com.practice.main.common.exception.DomainException;
import org.springframework.http.HttpStatus;

public class InvalidSessionException extends DomainException {
    public InvalidSessionException() {
        super("exception.invalid_session", null, HttpStatus.UNAUTHORIZED.value());
    }
}
