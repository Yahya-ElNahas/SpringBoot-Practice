package com.practice.test.Infrastructure.Exceptions;

import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends GeneralException {
    public InvalidRefreshTokenException() {
        super("exception.invalid_refresh_token", null, HttpStatus.UNAUTHORIZED);
    }
}
