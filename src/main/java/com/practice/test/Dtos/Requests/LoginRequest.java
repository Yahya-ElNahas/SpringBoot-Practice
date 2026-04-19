package com.practice.test.Dtos.Requests;

import com.practice.test.Infrastructure.Annotations.Password;

public record LoginRequest(String email, @Password String password) {}