package com.practice.main.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;
}

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumns({
//            @JoinColumn(name = "address_mobile", referencedColumnName = "mobile"),
//            @JoinColumn(name = "address_street", referencedColumnName = "street")
//    })
//    @Setter
//    private UserAddress address;