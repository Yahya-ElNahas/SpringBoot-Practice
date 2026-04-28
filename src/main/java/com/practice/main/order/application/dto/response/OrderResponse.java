package com.practice.main.order.application.dto.response;

import com.practice.main.order.application.dto.internal.OrderItemResponse;

import java.util.List;
import java.util.UUID;

public record OrderResponse (
    UUID id,
    List<OrderItemResponse> items,
    double totalPrice
) {}
