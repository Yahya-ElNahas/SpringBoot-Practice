package com.practice.test.Entities.Session;

import com.practice.test.Entities.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private User user;

    @Column(nullable = false, unique = true)
    @Setter
    private String token;

    @Column(name = "expiry_date", nullable = false)
    @Setter
    private LocalDateTime expiryDate;

    @Setter
    private boolean revoked;
}
