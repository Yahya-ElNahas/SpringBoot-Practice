package com.practice.main.user.application.dto.request;

import com.practice.main.user.domain.UserRole;
import com.practice.main.user.application.validation.password.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(

    @NotBlank(message = "{required.name}")
    String name,

    @NotBlank(message = "{required.email}")
    @Email(message = "{exception.invalid_email}")
    String email,

    @NotBlank(message = "{required.password}")
    @Password(min = 4)
    String password,

    @NotNull(message = "{required.role}")
    UserRole role
) {}