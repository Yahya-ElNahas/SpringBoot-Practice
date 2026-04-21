package com.practice.test.Repositories;

import com.practice.test.Entities.Session;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<@NonNull Session, @NonNull UUID> {

    Optional<Session> findByToken(String token);
}