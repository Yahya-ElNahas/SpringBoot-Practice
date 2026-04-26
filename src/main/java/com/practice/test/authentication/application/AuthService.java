package com.practice.test.authentication.application;

import com.practice.test.authentication.domain.Session;
import com.practice.test.authentication.infrastructure.SessionRepository;
import com.practice.test.common.util.RefreshTokenUtil;
import com.practice.test.authentication.application.dto.response.AuthTokens;
import com.practice.test.user.application.dto.UserMapper;
import com.practice.test.user.application.dto.request.CreateUserRequest;
import com.practice.test.authentication.application.dto.request.LoginRequest;
import com.practice.test.user.application.dto.response.UserResponse;
import com.practice.test.common.exception.EmailExistsException;
import com.practice.test.common.exception.IncorrectCredentialsException;
import com.practice.test.common.exception.InvalidSessionException;
import com.practice.test.user.domain.User;
import com.practice.test.user.Infrastructure.UserRepository;
import com.practice.test.security.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    private final TokenService tokenService;
    private final RefreshTokenUtil refreshTokenUtil;

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(CreateUserRequest body) {
        if(userRepository.existsByEmail(body.email())) {
            throw new EmailExistsException();
        }

        User user = User.builder()
                .name(body.name())
                .email(body.email())
                .password(passwordEncoder.encode(body.password()))
                .role(body.role())
                .build();
        User savedUser = userRepository.save(user);

        log.info("User created: id={} | email={}", savedUser.getId(), savedUser.getEmail());

        return userMapper.toUserResponse(savedUser);
    }

    public AuthTokens login(LoginRequest body) {
        User user = userRepository.findByEmail(body.email())
                .orElseThrow(IncorrectCredentialsException::new);

        if(!passwordEncoder.matches(body.password(), user.getPassword())) {
            log.warn("Login failed with email: {}", user.getEmail());
            throw new IncorrectCredentialsException();
        }

        String refreshToken = refreshTokenUtil.generateRefreshToken();

        Session session = Session.builder()
                .user(user)
                .token(refreshTokenUtil.hash(refreshToken))
                .expiryDate(LocalDateTime.now().plusHours(1))
                .build();
        Session savedSession = sessionRepository.save(session);

        String accessToken = tokenService.generateToken(
                user.getId(),
                user.getRole().name(),
                savedSession.getId()
        );

        return new AuthTokens(accessToken, refreshToken);
    }

    public AuthTokens refreshAccessToken(String refreshToken) {
        String hashedToken = refreshTokenUtil.hash(refreshToken);

        Session session = sessionRepository.findByToken(hashedToken)
                .orElseThrow(InvalidSessionException::new);

        if(session.isRevoked()
                || session.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidSessionException();
        }

        User user = session.getUser();

        String newRefreshToken = refreshTokenUtil.generateRefreshToken();

        session.setToken(refreshTokenUtil.hash(newRefreshToken));
        session.setExpiryDate(LocalDateTime.now().plusHours(1));
        Session savedSession = sessionRepository.save(session);

        String accessToken = tokenService.generateToken(
                user.getId(),
                user.getRole().name(),
                savedSession.getId()
        );

        return new AuthTokens(accessToken, newRefreshToken);
    }

    public void logout(String refreshToken) {
        String hashedToken = refreshTokenUtil.hash(refreshToken);

        Session session = sessionRepository.findByToken(hashedToken)
                .orElseThrow(InvalidSessionException::new);
        session.setRevoked(true);
        sessionRepository.save(session);
    }
}