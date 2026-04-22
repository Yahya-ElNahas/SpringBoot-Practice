package com.practice.test.Dtos.Responses;

import com.practice.test.Dtos.Internal.CartItemResponse;

import java.util.List;
import java.util.UUID;

public record UserResponse(UUID id, String name, String email, List<CartItemResponse> cart) {}