package com.practice.test.Infrastructure.Exceptions;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends GeneralException {
    public ProductNotFoundException() {
        super("Product not found", HttpStatus.NOT_FOUND);
    }
}