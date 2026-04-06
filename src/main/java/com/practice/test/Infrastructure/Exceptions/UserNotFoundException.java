package com.practice.test.Infrastructure.Exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CodedException {
    public UserNotFoundException() {
        super("User not found", HttpStatus.NOT_FOUND);
    }
}
