package com.practice.test.Dtos.Responses;

import com.practice.test.Entities.Cart.CartItem;

import java.util.List;

public record CartResponse(List<CartItem> cart) {}