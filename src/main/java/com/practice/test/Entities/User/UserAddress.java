package com.practice.test.Entities.User;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_addresses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserAddress {

    @EmbeddedId
    private UserAddressCK userAddressCK;

    private String city;
    private String state;
}
