package com.practice.test.Application.Services;

import com.practice.test.Application.Dtos.CreateUserRequest;
import com.practice.test.Infrastructure.Persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public final class UserService {
    private final UserRepository userRepository;

    public String createUser(CreateUserRequest body) throws Exception {
        if(body.name == null) throw new Exception("Name cannot be null");
        if(body.email == null) throw new Exception("Email cannot be null");
        if(body.password == null) throw new Exception("Password cannot be null");

        return userRepository.createUser(body.name, body.email, body.password + "(hashed)");
    }
}
