package com.practice.test.authentication.infrastructure;

import com.practice.test.authentication.domain.Session;
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
    @Cacheable(value = "sessions", key = "#uuid")
    Optional<Session> findById(UUID uuid);

    @Override
    @CacheEvict(value = "sessions", key = "#entity.id")
    <S extends Session> S save(S entity);

    Optional<Session> findByToken(String token);

    @Query("""
        UPDATE Session SET revoked = true WHERE userId = :userId and id != :sessionId
    """)
    @Modifying
    @CacheEvict(value = "sessions", allEntries = true)
    void revokeAllByUserIdExcept(UUID userId, UUID sessionId);
}