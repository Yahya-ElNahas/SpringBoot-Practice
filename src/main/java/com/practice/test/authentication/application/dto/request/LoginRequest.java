package com.practice.test.authentication.application.dto.request;

import com.practice.test.common.validation.password.Password;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "required.email")
        String email,

        @Password
        String password
) {}