package com.practice.test.Infrastructure.Exceptions;

import org.springframework.http.HttpStatus;

public class RefreshTokenNotProvidedException extends CodedException {
    public RefreshTokenNotProvidedException() {
        super("Refresh token is required", HttpStatus.UNAUTHORIZED);
    }
}
