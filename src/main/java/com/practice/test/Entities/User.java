package com.practice.test.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.scheduling.annotation.Async;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Entity
@Table(name = "Users")
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

    @Async
    public void initProfile() {
        System.out.println("profile initialized");
    }
}
