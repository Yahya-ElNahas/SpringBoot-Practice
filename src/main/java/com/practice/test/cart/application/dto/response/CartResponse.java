package com.practice.test.cart.application.dto.response;

import java.util.List;

public record CartResponse(List<CartItemResponse> cart) {}