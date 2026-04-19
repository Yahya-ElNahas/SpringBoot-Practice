package com.practice.test.Dtos.Requests;

import com.practice.test.Entities.User.UserRole;
import com.practice.test.Infrastructure.Annotations.Password;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(

    @NotBlank
    String name,

    @NotBlank
    @Email
    String email,

    @NotBlank
    @Password
    String password,

    @NotNull
    UserRole role
) {}