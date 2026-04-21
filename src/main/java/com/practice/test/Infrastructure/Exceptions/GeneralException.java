package com.practice.test.Infrastructure.Exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GeneralException extends RuntimeException {

    private final Object[] args;
    private final HttpStatus status;

    public GeneralException(final String message, final Object[] args, final HttpStatus status) {
        super(message);
        this.args = args;
        this.status = status;
    }
}
