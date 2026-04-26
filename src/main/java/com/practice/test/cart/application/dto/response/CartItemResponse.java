package com.practice.test.cart.application.dto.response;

import com.practice.test.product.application.dto.response.ProductResponse;

import java.util.UUID;

public record CartItemResponse(
        UUID id,
        ProductResponse product,
        int quantity
) {}
