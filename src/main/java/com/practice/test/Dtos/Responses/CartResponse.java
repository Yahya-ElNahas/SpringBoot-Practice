package com.practice.test.Dtos.Responses;

import com.practice.test.Dtos.Internal.CartItemResponse;

import java.util.List;

public record CartResponse(List<CartItemResponse> cart) {}