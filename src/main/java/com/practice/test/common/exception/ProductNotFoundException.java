package com.practice.test.common.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends GeneralException {
    public ProductNotFoundException() {
        super("exception.product_not_found", null, HttpStatus.NOT_FOUND);
    }
}