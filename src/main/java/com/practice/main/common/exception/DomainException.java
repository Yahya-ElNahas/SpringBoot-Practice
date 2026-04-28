package com.practice.main.common.exception;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

    private final Object[] args;
    private final int status;

    public DomainException(String message, Object[] args, int status) {
        super(message);
        this.args = args;
        this.status = status;
    }
}