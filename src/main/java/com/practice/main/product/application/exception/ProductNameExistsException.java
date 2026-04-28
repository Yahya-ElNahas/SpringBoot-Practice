package com.practice.main.product.application.exception;

import com.practice.main.common.exception.DomainException;
import org.springframework.http.HttpStatus;

public class ProductNameExistsException extends DomainException {
    public ProductNameExistsException() {
        super("exception.product_name_exists", null, HttpStatus.CONFLICT.value());
    }
}
