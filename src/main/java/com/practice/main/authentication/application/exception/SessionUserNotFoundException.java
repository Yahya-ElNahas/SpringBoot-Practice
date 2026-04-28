package com.practice.main.authentication.application.exception;

import com.practice.main.common.exception.DomainException;
import org.springframework.http.HttpStatus;

public class SessionUserNotFoundException extends DomainException {
    public SessionUserNotFoundException() {
        super("exception.user_not_found", null, HttpStatus.NOT_FOUND.value());
    }
}
