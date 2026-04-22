package com.practice.test.Dtos.Internal;

import com.practice.test.Dtos.Responses.ProductResponse;

import java.util.UUID;

public record CartItemResponse(
        UUID id,
        ProductResponse product,
        int quantity
) {}
