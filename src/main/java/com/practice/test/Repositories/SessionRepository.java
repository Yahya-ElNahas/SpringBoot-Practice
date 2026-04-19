package com.practice.test.Repositories;

import com.practice.test.Entities.Session.Session;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<@NonNull Session, @NonNull Integer> {

    Optional<Session> findByToken(String token);
}