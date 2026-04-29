package com.practice.main.order.application.dto.internal;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderPlacedEvent(
        UUID orderId,
        UUID userId,
        List<OrderItemDto> items,
        BigDecimal totalPrice
) {}
