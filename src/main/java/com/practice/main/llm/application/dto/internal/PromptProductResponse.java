package com.practice.main.llm.application.dto.internal;

import java.util.UUID;

public record PromptProductResponse(
        String id,
        String name,
        double price,
        int stock
) {}