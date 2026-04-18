package com.practice.test.Repositories;

import com.practice.test.Entities.User.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<@NonNull User, @NonNull Integer> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}