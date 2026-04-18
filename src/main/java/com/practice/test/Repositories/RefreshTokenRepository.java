package com.practice.test.Repositories;

import com.practice.test.Entities.RefreshToken.RefreshToken;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<@NonNull RefreshToken, @NonNull Integer> {

    Optional<RefreshToken> findByToken(String token);
}