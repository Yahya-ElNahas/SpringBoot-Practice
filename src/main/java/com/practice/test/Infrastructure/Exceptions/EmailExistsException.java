package com.practice.test.Infrastructure.Exceptions;

import org.springframework.http.HttpStatus;

public class EmailExistsException extends CodedException {
    public EmailExistsException() {
        super("Email already exists", HttpStatus.CONFLICT);
    }
}
