package com.practice.test.Dtos.Requests;

import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record AddToCartRequest(UUID productId, @Positive int quantity) {}