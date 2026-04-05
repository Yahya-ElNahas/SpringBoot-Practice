package com.practice.test.Application.Services;

import com.practice.test.Application.Dtos.CreateUserRequest;
import com.practice.test.Application.Dtos.UserResponse;
import com.practice.test.Domain.Entities.User;
import com.practice.test.Infrastructure.Persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse createUser(CreateUserRequest body) {
        User user = new User(body.name, body.email, body.password);
        User savedUser = userRepository.save(user);

        savedUser.initProfile();

        return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }

    @Transactional
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
