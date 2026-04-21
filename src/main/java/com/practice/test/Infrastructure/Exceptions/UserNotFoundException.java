package com.practice.test.Infrastructure.Exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GeneralException {
    public UserNotFoundException() {
        super("exception.user_not_found", null, HttpStatus.NOT_FOUND);
    }
}