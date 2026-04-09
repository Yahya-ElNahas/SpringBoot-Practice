package com.practice.test.Controller;

import com.practice.test.Dtos.Requests.UpdateUserAddressRequest;
import com.practice.test.Dtos.Responses.AllProductsResponse;
import com.practice.test.Dtos.Requests.CreateProductRequest;
import com.practice.test.Dtos.Requests.CreateUserRequest;
import com.practice.test.Dtos.Responses.AllUsersResponse;
import com.practice.test.Dtos.Responses.UserResponse;
import com.practice.test.Service.Service;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class Controller {

    private final Service service;

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        UserResponse result = service.createUser(createUserRequest);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/users/updateAddress")
    public ResponseEntity<UserResponse> updateUserAddress(@RequestBody UpdateUserAddressRequest updateUserAddressRequest) {
        UserResponse result = service.updateUserAddress(updateUserAddressRequest);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/users", params = "email")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam(name = "email") String email) {
        UserResponse result = service.getUserByEmail(email);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/users")
    public ResponseEntity<AllUsersResponse> AllUsers() {
        AllUsersResponse result = service.getAllUsers();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/products/createTable")
    public ResponseEntity<String> createProductsTable() {
        String result = service.createProductsTable();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/products")
    public ResponseEntity<String> createProduct(@RequestBody CreateProductRequest body) {
        String result = service.createProduct(body);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/products")
    public ResponseEntity<AllProductsResponse> getAllProducts() {
        AllProductsResponse result = service.getAllProducts();
        return ResponseEntity.ok(result);
    }
}