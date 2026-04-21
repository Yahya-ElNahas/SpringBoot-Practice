package com.practice.test.Dtos.Responses;

import java.util.List;

public record AllUsersResponse(
        List<UserResponse> users,
        int page,
        int size,
        long totalUsers,
        int totalPages
) {}