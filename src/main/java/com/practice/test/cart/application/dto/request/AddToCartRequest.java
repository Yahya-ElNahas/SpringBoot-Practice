package com.practice.test.cart.application.dto.request;

import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record AddToCartRequest(UUID productId, @Positive int quantity) {}