package com.practice.test.Service;

import com.practice.test.Dtos.Requests.CreateProductRequest;
import com.practice.test.Dtos.Requests.CreateUserRequest;
import com.practice.test.Dtos.Requests.UpdateUserAddressRequest;
import com.practice.test.Dtos.Responses.AllProductsResponse;
import com.practice.test.Dtos.Responses.AllUsersResponse;
import com.practice.test.Dtos.Responses.UserResponse;
import com.practice.test.Entities.User.User;
import com.practice.test.Entities.User.UserAddress;
import com.practice.test.Entities.User.UserAddressCK;
import com.practice.test.Infrastructure.Exceptions.AddressExistsException;
import com.practice.test.Infrastructure.Exceptions.EmailExistsException;
import com.practice.test.Infrastructure.Exceptions.UserNotFoundException;
import com.practice.test.Repositories.ProductRepository;
import com.practice.test.Repositories.UserAddressRepository;
import com.practice.test.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class Service {

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final ProductRepository productRepository;

    @Transactional
    public UserResponse createUser(CreateUserRequest body) {
        if(userRepository.existsByEmail(body.email)) {
            throw new EmailExistsException();
        }

        User user = new User(0, body.name, body.email, body.password, null);
        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), null);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), null);
    }

    @Transactional
    public UserResponse updateUserAddress(UpdateUserAddressRequest body) {
        if(userAddressRepository.existsById(new UserAddressCK(body.mobile, body.street))) {
            throw new AddressExistsException();
        }

        UserAddress address = new UserAddress(
                new UserAddressCK(body.mobile, body.street),
                body.city,
                body.state
        );
        UserAddress savedAddress = userAddressRepository.save(address);

        User user = userRepository.findById(body.userId)
                .orElseThrow(UserNotFoundException::new);
        user.setAddress(savedAddress);
        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getAddress());
    }

    public AllUsersResponse getAllUsers() {
        return new AllUsersResponse(userRepository.findAll());
    }

    public String createProductsTable() {
        productRepository.createProductsTable();
        return "Success";
    }

    public String createProduct(CreateProductRequest body) {
        productRepository.createProduct(body.name, body.price, body.stock);
        return "Success";
    }

    public AllProductsResponse getAllProducts() {
        return new AllProductsResponse(productRepository.getAllProducts());
    }
}
