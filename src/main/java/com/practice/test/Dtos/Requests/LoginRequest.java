package com.practice.test.Dtos.Requests;

import com.practice.test.Infrastructure.Annotations.Password;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "required.email")
        String email,

        @Password
        String password
) {}