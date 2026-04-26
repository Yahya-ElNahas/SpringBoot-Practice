package com.practice.test.product.application.dto.response;

import java.util.UUID;

public record ProductResponse(UUID id, UUID createdBy, String name, double price, int stock) {}