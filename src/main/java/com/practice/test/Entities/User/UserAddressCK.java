package com.practice.test.Entities.User;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class UserAddressCK implements Serializable {

    private String mobile;
    private String street;
}
