package com.practice.test.Repositories;

import com.practice.test.Entities.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<@NonNull User, @NonNull UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}