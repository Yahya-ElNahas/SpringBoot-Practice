package com.practice.test.Domain.Builders;

import com.practice.test.Domain.Entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserBuilder {
    private static int userCount = 0;

    public UserBuilder() {
        System.out.println("created user builder");
    }

    public User createUser(final String name, final String email, final String password) {
        return new User(++userCount, name, email, password);
    }
}
