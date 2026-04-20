package com.practice.test.Infrastructure.Exceptions;

import org.springframework.http.HttpStatus;

public class InsufficientStockException extends GeneralException {
    public InsufficientStockException(int stock) {
        super("Insufficient stock available, current stock: " + stock, HttpStatus.BAD_REQUEST);
    }
}
