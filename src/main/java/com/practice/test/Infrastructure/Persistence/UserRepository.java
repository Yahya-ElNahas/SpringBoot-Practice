package com.practice.test.Infrastructure.Persistence;

import com.practice.test.Application.Interfaces.IUserRepository;
import com.practice.test.Domain.Builders.UserBuilder;
import com.practice.test.Domain.Entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public final class UserRepository implements IUserRepository {
    private final UserBuilder userBuilder;

    @Override
    public String createUser(String name, String email, String password) {
        User user = userBuilder.createUser(name, email, password);
        System.out.println("User Repo Created: " + user);
        return user.toString();
    }
}
