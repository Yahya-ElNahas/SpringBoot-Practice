package com.practice.main.product.application.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        UUID createdBy,
        String name,
        double price,
        int stock,
        Instant createdAt
) {}