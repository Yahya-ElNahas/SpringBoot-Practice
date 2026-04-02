package com.practice.test.Domain.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class User {
    private @Setter int id;
    private @Setter String name;
    private String email;
    @ToString.Exclude
    private final String password;
}
