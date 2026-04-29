package com.practice.main.order.application.dto.internal;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemDto(
        UUID productId,
        String name,
        BigDecimal price,
        int quantity,
        BigDecimal subTotal
) {}