package com.practice.main.cart.application.dto.response;

import com.practice.main.product.application.dto.response.ProductResponse;

import java.util.UUID;

public record CartItemResponse(
        UUID id,
        ProductResponse product,
        int quantity
) {}
