package com.practice.main.user.application.dto.response;

import java.util.List;

public record AllUsersResponse(
        List<UserResponse> users,
        int page,
        int size,
        long totalUsers,
        int totalPages
) {}