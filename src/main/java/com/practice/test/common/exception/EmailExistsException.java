package com.practice.test.common.exception;

import org.springframework.http.HttpStatus;

public class EmailExistsException extends GeneralException {
    public EmailExistsException() {
        super("exception.email_exists", null, HttpStatus.CONFLICT);
    }
}