package com.practice.test.Infrastructure.Exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GeneralException {
    public UserNotFoundException() {
        super("User not found", HttpStatus.NOT_FOUND);
    }
}