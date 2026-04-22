package com.practice.test.Entities;

import com.practice.test.Entities.Enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @OrderBy("addedAt ASC")
    private List<CartItem> cart;
}

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumns({
//            @JoinColumn(name = "address_mobile", referencedColumnName = "mobile"),
//            @JoinColumn(name = "address_street", referencedColumnName = "street")
//    })
//    @Setter
//    private UserAddress address;