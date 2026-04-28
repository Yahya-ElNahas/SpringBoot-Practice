package com.practice.main.user.application.exception;

import com.practice.main.common.exception.DomainException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException() {
        super("exception.user_not_found", null, HttpStatus.NOT_FOUND.value());
    }
}