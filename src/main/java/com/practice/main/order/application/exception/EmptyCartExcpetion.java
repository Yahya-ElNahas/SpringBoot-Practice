package com.practice.main.order.application.exception;

import com.practice.main.common.exception.DomainException;
import org.springframework.http.HttpStatus;

public class EmptyCartExcpetion extends DomainException {
    public EmptyCartExcpetion() {
        super("exception.empty_cart", null, HttpStatus.BAD_REQUEST.value());
    }
}
