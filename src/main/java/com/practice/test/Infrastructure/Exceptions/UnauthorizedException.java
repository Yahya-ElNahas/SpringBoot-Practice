package com.practice.test.Infrastructure.Exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends GeneralException {
    public UnauthorizedException() {
        super("Unauthorized", HttpStatus.UNAUTHORIZED);
    }
}
