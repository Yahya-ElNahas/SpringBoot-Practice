package com.practice.main.cart.application.exception;

import com.practice.main.common.exception.DomainException;
import org.springframework.http.HttpStatus;

public class InsufficientStockException extends DomainException {
    public InsufficientStockException(int stock) {
        super("exception.insufficient_stock", new Object[]{stock}, "CART", "002", "INSUFFICIENT_STOCK");
    }
}