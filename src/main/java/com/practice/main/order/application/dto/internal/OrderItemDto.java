package com.practice.main.order.application.dto.internal;

import java.math.BigDecimal;

public record OrderItemDto(
        String productId,
        String name,
        BigDecimal price,
        int quantity,
        BigDecimal subTotal
) {}