package com.practice.test.authentication.infrastructure;

import com.practice.test.authentication.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {

    Optional<Session> findByToken(String token);
}