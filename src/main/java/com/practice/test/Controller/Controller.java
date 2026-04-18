package com.practice.test.Controller;

import com.practice.test.Dtos.Requests.LoginRequest;
import com.practice.test.Dtos.Requests.UpdateUserAddressRequest;
import com.practice.test.Dtos.Responses.AllProductsResponse;
import com.practice.test.Dtos.Requests.CreateProductRequest;
import com.practice.test.Dtos.Requests.CreateUserRequest;
import com.practice.test.Dtos.Responses.AllUsersResponse;
import com.practice.test.Dtos.Responses.AuthResponse;
import com.practice.test.Dtos.Responses.UserResponse;
import com.practice.test.Service.IService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class Controller {

    private final IService service;

    @PostMapping("/auth/login")
    public ResponseEntity<@NonNull AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse result = service.login(loginRequest);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<@NonNull UserResponse> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        UserResponse result = service.createUser(createUserRequest);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<@NonNull String> logout(HttpServletRequest request) {
        String result = service.logout(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/auth/refreshToken")
    public ResponseEntity<@NonNull String> refreshToken(HttpServletRequest request) {
        String result = service.refreshAccessToken(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/users/updateAddress")
    public ResponseEntity<@NonNull UserResponse> updateUserAddress(@RequestBody UpdateUserAddressRequest updateUserAddressRequest) {
        UserResponse result = service.updateUserAddress(updateUserAddressRequest);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/users", params = "email")
    public ResponseEntity<@NonNull UserResponse> getUserByEmail(@RequestParam(name = "email") String email) {
        UserResponse result = service.getUserByEmail(email);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/users")
    public ResponseEntity<@NonNull AllUsersResponse> AllUsers() {
        AllUsersResponse result = service.getAllUsers();
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/products/createTable")
    public ResponseEntity<@NonNull String> createProductsTable() {
        String result = service.createProductsTable();
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/products")
    public ResponseEntity<@NonNull String> createProduct(@RequestBody CreateProductRequest createProductRequest) {
        String result = service.createProduct(createProductRequest);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/products")
    public ResponseEntity<@NonNull AllProductsResponse> getAllProducts() {
        AllProductsResponse result = service.getAllProducts();
        return ResponseEntity.ok(result);
    }
}