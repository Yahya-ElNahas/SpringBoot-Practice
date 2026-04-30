package com.practice.main.common.exception;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

    private final Object[] args;
    private final String code;

    public DomainException(String message, Object[] args, String feature, String status, String code) {
        super(message);
        this.args = args;
        this.code = feature.toUpperCase() + '_' + status + '_' + code;
    }
}