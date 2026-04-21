package com.practice.test.Infrastructure.Exceptions;

import org.springframework.http.HttpStatus;

public class InsufficientStockException extends GeneralException {
    public InsufficientStockException(int stock) {
        super("exception.insufficient_stock", new Object[]{stock}, HttpStatus.BAD_REQUEST);
    }
}