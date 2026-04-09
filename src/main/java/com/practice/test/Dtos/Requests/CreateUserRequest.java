package com.practice.test.Dtos.Requests;

import com.practice.test.Infrastructure.Annotations.Password;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateUserRequest {
    public final String name;
    public final String email;
    @Password
    public final String password;
}
