package com.practice.main.user.application.dto.response;

import java.util.UUID;

public record UserResponse(UUID id, String name, String email) {}