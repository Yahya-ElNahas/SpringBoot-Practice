package com.practice.test.Service;

import com.practice.test.Dtos.Requests.CreateProductRequest;
import com.practice.test.Dtos.Requests.CreateUserRequest;
import com.practice.test.Dtos.Responses.AllProductsResponse;
import com.practice.test.Dtos.Responses.AllUsersResponse;
import com.practice.test.Dtos.Responses.UserResponse;
import com.practice.test.Entities.User;
import com.practice.test.Infrastructure.Exceptions.EmailExistsException;
import com.practice.test.Infrastructure.Exceptions.UserNotFoundException;
import com.practice.test.Repositories.ProductRepository;
import com.practice.test.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class Service {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public UserResponse createUser(CreateUserRequest body) {
        User user = new User(0, body.name, body.email, body.password);

        try {
            User savedUser = userRepository.save(user);

            savedUser.initProfile();

            return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
        } catch (DataIntegrityViolationException ex) {
            if(ex.getMessage().contains("PUBLIC.USERS(EMAIL NULLS FIRST)")) {
                throw new EmailExistsException();
            }
            return null;
        }
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
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
