package com.practice.test.Dtos.Internal;

import com.practice.test.Entities.Product.Product;

public record CartItemResponse(
        int id,
        Product product,
        int quantity
) {}
