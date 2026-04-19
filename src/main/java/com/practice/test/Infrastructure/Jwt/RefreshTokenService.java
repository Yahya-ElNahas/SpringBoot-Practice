package com.practice.test.Infrastructure.Jwt;

import com.practice.test.Entities.Session.Session;
import com.practice.test.Entities.User.User;
import com.practice.test.Infrastructure.Exceptions.UnauthorizedException;
import com.practice.test.Repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public Session createRefreshToken(User user) {
        String token = jwtService.generateRefreshToken(user.getEmail());

        Session refreshToken = new Session(
                0,
                user,
                token,
                jwtService.extractExpirationDate(token).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                false
        );
        return refreshTokenRepository.save(refreshToken);
    }

    public Session findByToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(UnauthorizedException::new);
    }

    public boolean isTokenExpired(Session refreshToken) {
        return refreshToken.getExpiryDate().isBefore(LocalDateTime.from(Instant.now()));
    }
}
