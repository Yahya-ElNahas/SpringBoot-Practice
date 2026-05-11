package com.practice.main.authentication.infrastructure;

import com.practice.main.authentication.domain.Session;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {

    @Override
    Optional<Session> findById(UUID uuid);

    @Override
    <S extends Session> S save(S entity);

    Optional<Session> findByToken(String token);

    @Query(value = """
        UPDATE Sessions
        SET revoked = true
        WHERE user_id = :userId and revoked = false
        RETURNING id
    """, nativeQuery = true)
    @Modifying
    List<UUID> revokeAllByUserId(UUID userId);
}