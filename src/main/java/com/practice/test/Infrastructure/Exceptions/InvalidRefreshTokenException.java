package com.practice.test.Infrastructure.Exceptions;

import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends GeneralException {
    public InvalidRefreshTokenException() {
        super("Invalid or expired refresh token", HttpStatus.UNAUTHORIZED);
    }
}
