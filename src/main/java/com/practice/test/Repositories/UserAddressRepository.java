package com.practice.test.Repositories;

import com.practice.test.Entities.User.UserAddress;
import com.practice.test.Entities.User.UserAddressCK;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<@NonNull UserAddress, @NonNull UserAddressCK> {

}
