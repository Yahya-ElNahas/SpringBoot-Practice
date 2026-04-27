package com.practice.test.cart.application.exception;

import com.practice.test.common.exception.DomainException;
import org.springframework.http.HttpStatus;

public class InsufficientStockException extends DomainException {
    public InsufficientStockException(int stock) {
        super("exception.insufficient_stock", new Object[]{stock}, HttpStatus.BAD_REQUEST.value());
    }
}