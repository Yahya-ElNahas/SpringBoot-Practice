package com.practice.main.authentication.application.dto.request;

import com.practice.main.user.application.validation.password.Password;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "required.email")
        String email,

        @Password
        String password
) {}