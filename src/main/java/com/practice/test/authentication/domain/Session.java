package com.practice.test.authentication.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    private UUID userId;

    @Setter
    private String userRole;

    @Column(nullable = false, unique = true)
    @Setter
    private String token;

    @Column(name = "expiry_date", nullable = false)
    @Setter
    private Instant expiryDate;

    @Setter
    private boolean revoked = false;
}