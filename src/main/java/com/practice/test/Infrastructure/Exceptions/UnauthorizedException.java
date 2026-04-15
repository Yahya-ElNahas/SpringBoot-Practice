package com.practice.test.Infrastructure.Exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends CodedException {
    public UnauthorizedException() {
        super("Unauthorized", HttpStatus.UNAUTHORIZED);
    }
}
