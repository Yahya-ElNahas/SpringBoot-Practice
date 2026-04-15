package com.practice.test.Infrastructure.Jwt;

import com.practice.test.Entities.RefreshToken.RefreshToken;
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

    public RefreshToken createRefreshToken(User user) {
        String token = jwtService.generateRefreshToken(user.getEmail());

        RefreshToken refreshToken = new RefreshToken(
                0,
                user,
                token,
                jwtService.extractExpirationDate(token).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                false
        );
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(UnauthorizedException::new);
    }

    public boolean isTokenExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiryDate().isBefore(LocalDateTime.from(Instant.now()));
    }
}
