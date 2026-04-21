package com.practice.test.Dtos.Internal;

import com.practice.test.Entities.Product;

import java.util.UUID;

public record CartItemResponse(
        UUID id,
        Product product,
        int quantity
) {}
