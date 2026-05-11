package com.practice.main.authentication.application;

import com.practice.main.authentication.application.util.SessionService;
import com.practice.main.authentication.domain.Session;
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
import com.practice.main.security.token.AccessTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final SessionService sessionService;
    private final AccessTokenService accessTokenService;

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

        Session session = sessionService.createSession(user.getId(), user.getRole().name());

        String accessToken = accessTokenService.generateToken(
                user.getId(),
                user.getRole().name(),
                session.getId()
        );

        return new AuthTokens(accessToken, session.getToken());
    }

    @Transactional
    @LoggingEvent("REFRESH_TOKEN")
    public AuthTokens refreshAccessToken(String refreshToken) {

        Session session = sessionService.refreshToken(refreshToken);

        String accessToken = accessTokenService.generateToken(
                session.getUserId(),
                session.getUserRole(),
                session.getId()
        );

        return new AuthTokens(accessToken, session.getToken());
    }

    @Transactional
    @LoggingEvent("LOGOUT")
    public void logout(String refreshToken) {
        sessionService.revokeSession(refreshToken);
    }
}