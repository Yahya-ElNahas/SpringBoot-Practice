package com.practice.test.Repositories;

import com.practice.test.Entities.User.UserAddress;
import com.practice.test.Entities.User.UserAddressCK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, UserAddressCK> {

}
