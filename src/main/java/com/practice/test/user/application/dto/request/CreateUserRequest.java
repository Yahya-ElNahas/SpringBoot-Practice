package com.practice.test.user.application.dto.request;

import com.practice.test.user.domain.UserRole;
import com.practice.test.user.application.validation.password.Password;
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