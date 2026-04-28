package com.practice.main.order.application.dto.internal;

import java.util.UUID;

public record OrderItemResponse(
        UUID productId,
        String productName,
        double price,
        double quantity,
        double subTotal
) {}