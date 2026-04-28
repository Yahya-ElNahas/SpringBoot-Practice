package com.practice.test.authentication.application.exception;

import com.practice.test.common.exception.DomainException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException() {
        super("exception.email_exists", null, HttpStatus.CONFLICT.value());
    }
}