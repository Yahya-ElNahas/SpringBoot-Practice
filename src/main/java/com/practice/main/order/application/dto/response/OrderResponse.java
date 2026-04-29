package com.practice.main.order.application.dto.response;

import com.practice.main.order.application.dto.internal.OrderItemDto;

import java.util.List;
import java.util.UUID;

public record OrderResponse (
    UUID id,
    int totalItems,
    double totalPrice
) {}
