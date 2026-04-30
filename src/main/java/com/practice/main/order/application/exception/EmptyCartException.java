package com.practice.main.order.application.exception;

import com.practice.main.common.exception.DomainException;

public class EmptyCartException extends DomainException {
    public EmptyCartException() {
        super("exception.empty_cart", null, "ORDER", "001", "EMPTY_CART");
    }
}
