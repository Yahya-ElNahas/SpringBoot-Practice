package com.practice.main.authentication.application.util;

import com.practice.main.authentication.application.dto.response.AuthTokens;
import com.practice.main.authentication.application.exception.InvalidSessionException;
import com.practice.main.authentication.domain.Session;
import com.practice.main.authentication.infrastructure.SessionRepository;
import com.practice.main.security.token.AccessTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;
    private final SessionCacheService cacheService;

    @Value("${auth.refresh-token.expiry-hours}")
    private int refreshTokenExpiryHours;

    public AuthTokens createSession(UUID userId, String userRole) {
        List<UUID> revokedSessions = sessionRepository.revokeAllByUserId(userId);

        cacheService.deleteBySessionIds(revokedSessions);

        String refreshToken = refreshTokenService.generateRefreshToken();

        Session session = Session.builder()
                .userId(userId)
                .userRole(userRole)
                .token(refreshTokenService.hash(refreshToken))
                .expiryDate(Instant.now().plus(refreshTokenExpiryHours, ChronoUnit.HOURS))
                .build();
        Session savedSession = sessionRepository.save(session);

        cacheService.cache(savedSession);

        String accessToken = accessTokenService.generateToken(
                savedSession.getUserId(),
                savedSession.getUserRole(),
                savedSession.getId()
        );

        return new AuthTokens(accessToken, refreshToken);
    }

    public AuthTokens refreshToken(String refreshToken) {
        String hashedToken = refreshTokenService.hash(refreshToken);

        Session session = sessionRepository.findByToken(hashedToken)
                .orElseThrow(InvalidSessionException::new);

        if(session.isRevoked() || session.getExpiryDate().isBefore(Instant.now())) {
            throw new InvalidSessionException();
        }

        UUID userId = session.getUserId();

        List<UUID> revokedSessions = sessionRepository.revokeAllByUserId(userId);

        cacheService.deleteBySessionIds(revokedSessions);

        String newRefreshToken = refreshTokenService.generateRefreshToken();

        session.setToken(refreshTokenService.hash(newRefreshToken));
        session.setRevoked(false);
        session.setExpiryDate(Instant.now().plus(refreshTokenExpiryHours, ChronoUnit.HOURS));
        Session savedSession = sessionRepository.save(session);

        cacheService.cache(savedSession);

        String accessToken = accessTokenService.generateToken(
                savedSession.getUserId(),
                savedSession.getUserRole(),
                savedSession.getId()
        );

        return new AuthTokens(accessToken, newRefreshToken);
    }

    public void revokeSession(String refreshToken) {
        String hashedToken = refreshTokenService.hash(refreshToken);

        Session session = sessionRepository.findByToken(hashedToken)
                .orElseThrow(InvalidSessionException::new);
        session.setRevoked(true);
        sessionRepository.save(session);

        cacheService.deleteBySessionIds(Stream.of(session.getId()).toList());
    }
}