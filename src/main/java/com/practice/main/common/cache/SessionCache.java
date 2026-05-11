package com.practice.main.common.cache;

import java.util.UUID;

public record SessionCache(
        UUID userId,
        String role,
        String token
) {}