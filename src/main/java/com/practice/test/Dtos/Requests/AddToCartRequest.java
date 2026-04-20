package com.practice.test.Dtos.Requests;

import jakarta.validation.constraints.Positive;

public record AddToCartRequest(int productId, @Positive int quantity) {}