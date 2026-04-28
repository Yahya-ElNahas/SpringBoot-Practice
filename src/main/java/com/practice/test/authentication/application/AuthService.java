package com.practice.test.authentication.application;

import com.practice.test.authentication.domain.Session;
import com.practice.test.authentication.infrastructure.SessionRepository;
import com.practice.test.security.token.RefreshTokenService;
import com.practice.test.authentication.application.dto.response.AuthTokens;
import com.practice.test.user.application.dto.UserMapper;
import com.practice.test.user.application.dto.request.CreateUserRequest;
import com.practice.test.authentication.application.dto.request.LoginRequest;
import com.practice.test.user.application.dto.response.UserResponse;
import com.practice.test.authentication.application.exception.EmailAlreadyExistsException;
import com.practice.test.authentication.application.exception.IncorrectCredentialsException;
import com.practice.test.authentication.application.exception.InvalidSessionException;
import com.practice.test.user.domain.User;
import com.practice.test.user.Infrastructure.UserRepository;
import com.practice.test.security.token.AccessTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Value("${auth.refresh-token.expiry-hours}")
    private int refreshTokenExpiryHours;

    @Transactional
    public UserResponse createUser(CreateUserRequest body) {

        User user = User.builder()
                .name(body.name())
                .email(body.email())
                .password(passwordEncoder.encode(body.password()))
                .role(body.role())
                .build();
        try {
            User savedUser = userRepository.save(user);

            log.info("User created: id={} | email={}", savedUser.getId(), savedUser.getEmail());

            return userMapper.toUserResponse(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistsException();
        }
    }

    @Transactional
    public AuthTokens login(LoginRequest body) {
        User user = userRepository.findByEmail(body.email())
                .orElseThrow(IncorrectCredentialsException::new);

        if(!passwordEncoder.matches(body.password(), user.getPassword())) {
            log.warn("Login failed with email: {}", user.getEmail());

            throw new IncorrectCredentialsException();
        }

        String refreshToken = refreshTokenService.generateRefreshToken();

        Session session = Session.builder()
                .userId(user.getId())
                .userRole(user.getRole().name())
                .token(refreshTokenService.hash(refreshToken))
                .expiryDate(Instant.now().plus(refreshTokenExpiryHours, ChronoUnit.HOURS))
                .build();
        Session savedSession = sessionRepository.save(session);

        sessionRepository.revokeAllByUserIdExcept(user.getId(), savedSession.getId());

        String accessToken = accessTokenService.generateToken(
                user.getId(),
                user.getRole().name(),
                savedSession.getId()
        );

        return new AuthTokens(accessToken, refreshToken);
    }

    @Transactional
    public AuthTokens refreshAccessToken(String refreshToken) {
        String hashedToken = refreshTokenService.hash(refreshToken);

        Session session = sessionRepository.findByToken(hashedToken)
                .orElseThrow(InvalidSessionException::new);

        if(session.isRevoked() || session.getExpiryDate().isBefore(Instant.now())) {
            throw new InvalidSessionException();
        }

        UUID userId = session.getUserId();

        sessionRepository.revokeAllByUserIdExcept(userId, session.getId());

        String newRefreshToken = refreshTokenService.generateRefreshToken();

        session.setToken(refreshTokenService.hash(newRefreshToken));
        session.setExpiryDate(Instant.now().plus(refreshTokenExpiryHours, ChronoUnit.HOURS));
        Session savedSession = sessionRepository.save(session);

        String accessToken = accessTokenService.generateToken(
                userId,
                session.getUserRole(),
                savedSession.getId()
        );

        return new AuthTokens(accessToken, newRefreshToken);
    }

    @Transactional
    public void logout(String refreshToken) {
        String hashedToken = refreshTokenService.hash(refreshToken);

        Session session = sessionRepository.findByToken(hashedToken)
                .orElseThrow(InvalidSessionException::new);
        session.setRevoked(true);
        sessionRepository.save(session);
    }
}