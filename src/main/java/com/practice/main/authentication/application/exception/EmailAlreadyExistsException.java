package com.practice.main.authentication.application.exception;

import com.practice.main.common.exception.DomainException;

public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException() {
        super("exception.email_exists", null, "AUTH", "002", "EMAIL_ALREADY_EXISTS");
    }
}