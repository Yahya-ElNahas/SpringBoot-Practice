package com.practice.test.Dtos.Requests;

import com.practice.test.Entities.User.UserRole;
import com.practice.test.Infrastructure.Annotations.Password;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank
    public final String name;

    @NotBlank
    @Email
    public final String email;

    @NotBlank
    @Password
    public final String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    public final UserRole role;
}