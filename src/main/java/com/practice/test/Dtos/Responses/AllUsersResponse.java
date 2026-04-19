package com.practice.test.Dtos.Responses;

import com.practice.test.Entities.User.User;

import java.util.List;

public record AllUsersResponse(List<User> users) {}