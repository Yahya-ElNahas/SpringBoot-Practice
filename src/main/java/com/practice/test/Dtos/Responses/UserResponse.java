package com.practice.test.Dtos.Responses;

import com.practice.test.Entities.CartItem;

import java.util.List;
import java.util.UUID;

public record UserResponse(UUID id, String name, String email, List<CartItem> cart) {}