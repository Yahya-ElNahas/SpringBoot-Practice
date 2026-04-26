package com.practice.test.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GeneralException extends RuntimeException {

    private final Object[] args;
    private final HttpStatus status;

    public GeneralException(String message, Object[] args, HttpStatus status) {
        super(message);
        this.args = args;
        this.status = status;
    }
}
