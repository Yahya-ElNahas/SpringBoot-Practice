package com.practice.test.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    @ManyToOne
    @Setter
    private User user;

    @Column(nullable = false, unique = true)
    @Setter
    private String token;

    @Column(name = "expiry_date", nullable = false)
    @Setter
    private LocalDateTime expiryDate;

    @Setter
    private boolean revoked = false;
}
