package com.practice.test.Dtos.Responses;

import java.util.UUID;

public record ProductResponse(UUID id, UUID createdBy, String name, double price, int stock) {}