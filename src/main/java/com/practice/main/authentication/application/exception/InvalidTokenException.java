package com.practice.main.authentication.application.exception;

import com.practice.main.common.exception.DomainException;

public class InvalidTokenException extends DomainException {
    public InvalidTokenException() {
        super("exception.invalid_token", null, "AUTH", "005", "INVALID_TOKEN");
    }
}
