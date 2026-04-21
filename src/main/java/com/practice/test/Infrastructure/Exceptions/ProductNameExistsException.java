package com.practice.test.Infrastructure.Exceptions;

import org.springframework.http.HttpStatus;

public class ProductNameExistsException extends GeneralException {
    public ProductNameExistsException() {
        super("exception.product_name_exists", null, HttpStatus.CONFLICT);
    }
}
