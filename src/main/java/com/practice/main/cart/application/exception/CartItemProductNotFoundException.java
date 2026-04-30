package com.practice.main.cart.application.exception;

import com.practice.main.common.exception.DomainException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class CartItemProductNotFoundException extends DomainException {
    public CartItemProductNotFoundException(UUID productId) {
        super("exception.cart_item_product_not_found", new Object[]{productId}, "CART", "001", "PRODUCT_NOT_FOUND");
    }
}