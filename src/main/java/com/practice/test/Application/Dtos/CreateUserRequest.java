package com.practice.test.Application.Dtos;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateUserRequest {
    public final String name;
    public final String email;
    public String password;
}
