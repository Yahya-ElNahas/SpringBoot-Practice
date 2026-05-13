package com.practice.main.authentication.application;

import com.practice.main.authentication.application.util.SessionService;
import com.practice.main.common.event.LoggingEvent;
import com.practice.main.authentication.application.dto.response.AuthTokens;
import com.practice.main.user.application.dto.UserMapper;
import com.practice.main.user.application.dto.request.CreateUserRequest;
import com.practice.main.authentication.application.dto.request.LoginRequest;
import com.practice.main.user.application.dto.response.UserResponse;
import com.practice.main.authentication.application.exception.EmailAlreadyExistsException;
import com.practice.main.authentication.application.exception.IncorrectCredentialsException;
import com.practice.main.user.domain.User;
import com.practice.main.user.Infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final SessionService sessionService;

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    @LoggingEvent("CREATE_USER")
    public UserResponse createUser(CreateUserRequest body) {
        if (userRepository.existsByEmail(body.email())) {
            throw new EmailAlreadyExistsException();
        }

        User user = User.builder()
                .name(body.name())
                .email(body.email())
                .password(passwordEncoder.encode(body.password()))
                .role(body.role())
                .build();

        User savedUser = userRepository.saveAndFlush(user);
        return userMapper.toUserResponse(savedUser);
    }

    @Transactional
    @LoggingEvent("LOGIN")
    public AuthTokens login(LoginRequest body) {
        User user = userRepository.findByEmail(body.email())
                .orElseThrow(IncorrectCredentialsException::new);

        if(!passwordEncoder.matches(body.password(), user.getPassword())) {
            throw new IncorrectCredentialsException();
        }

        return sessionService.createSession(user.getId(), user.getRole().name());
    }

    @Transactional
    @LoggingEvent("REFRESH_TOKEN")
    public AuthTokens refreshAccessToken(String refreshToken) {
        return sessionService.refreshToken(refreshToken);
    }

    @Transactional
    @LoggingEvent("LOGOUT")
    public void logout(String refreshToken) {
        sessionService.revokeSession(refreshToken);
    }
}