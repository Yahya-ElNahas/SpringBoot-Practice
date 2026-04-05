package com.practice.test.Domain.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.scheduling.annotation.Async;

@NoArgsConstructor
@ToString
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    public User(final String name, final String email, final String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Async
    public void initProfile() {
        System.out.println("profile initialized");
    }
}
