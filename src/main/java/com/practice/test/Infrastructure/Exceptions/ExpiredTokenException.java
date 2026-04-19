package com.practice.test.Infrastructure.Exceptions;

import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends GeneralException {
    public ExpiredTokenException() {
        super("Refresh token expired", HttpStatus.UNAUTHORIZED);
    }
}
