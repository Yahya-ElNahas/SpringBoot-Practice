package com.practice.test.Dtos.Responses;

import com.practice.test.Entities.User.UserAddress;

public record UserResponse(int id, String name, String email, UserAddress address) {}
