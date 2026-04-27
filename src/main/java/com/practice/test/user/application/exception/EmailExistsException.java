package com.practice.test.user.application.exception;

import com.practice.test.common.exception.DomainException;
import org.springframework.http.HttpStatus;

public class EmailExistsException extends DomainException {
    public EmailExistsException() {
        super("exception.email_exists", null, HttpStatus.CONFLICT.value());
    }
}