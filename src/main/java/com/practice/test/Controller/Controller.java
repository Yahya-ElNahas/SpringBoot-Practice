package com.practice.test.Controller;

import com.practice.test.Dtos.Internal.AuthTokens;
import com.practice.test.Dtos.Requests.AddToCartRequest;
import com.practice.test.Dtos.Requests.LoginRequest;
import com.practice.test.Dtos.Responses.*;
import com.practice.test.Dtos.Requests.CreateProductRequest;
import com.practice.test.Dtos.Requests.CreateUserRequest;
import com.practice.test.Service.CookieService;
import com.practice.test.Service.IService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@AllArgsConstructor
public class Controller {

    private final IService service;
    private final CookieService cookieService;

    @PostMapping("/auth/signup")
    public ResponseEntity<@NonNull UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest createUserRequestBody
    ) {
        UserResponse result = service.createUser(createUserRequestBody);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<@NonNull AuthResponse> login(
            @Valid @RequestBody LoginRequest loginRequestBody,
            HttpServletResponse response
    ) {
        AuthTokens tokens = service.login(loginRequestBody);
        cookieService.createRefreshTokenCookie(response, tokens.refreshToken());
        return ResponseEntity.ok(new AuthResponse(tokens.accessToken()));
    }

    @PostMapping("/auth/refreshToken")
    public ResponseEntity<@NonNull AuthResponse> refreshToken(
            @CookieValue(name = "refresh_token") String refreshToken,
            HttpServletResponse response
    ) {
        AuthTokens tokens = service.refreshAccessToken(refreshToken);
        cookieService.createRefreshTokenCookie(response, tokens.refreshToken());
        return ResponseEntity.ok(new AuthResponse(tokens.accessToken()));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<@NonNull String> logout(
            @CookieValue(name = "refresh_token") String refreshToken
    ) {
        String result = service.logout(refreshToken);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/users", params = "email")
    public ResponseEntity<@NonNull UserResponse> getUserByEmail(
            @RequestParam(name = "email") String email
    ) {
        UserResponse result = service.getUserByEmail(email);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/users")
    public ResponseEntity<@NonNull AllUsersResponse> AllUsers() {
        AllUsersResponse result = service.getAllUsers();
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/products")
    public ResponseEntity<@NonNull ProductResponse> createProduct(
            @RequestBody CreateProductRequest createProductRequestBody
    ) {
        ProductResponse result = service.createProduct(createProductRequestBody);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/products")
    public ResponseEntity<@NonNull AllProductsResponse> getAllProducts() {
        AllProductsResponse result = service.getAllProducts();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/cart/addToCart")
    public ResponseEntity<@NonNull CartResponse> addProductToCart(
            @RequestBody AddToCartRequest addToCartRequestBody
    ) {
        CartResponse response = service.addProductToCart(addToCartRequestBody);
        return ResponseEntity.ok(response);
    }
}

//    @PostMapping("/users/updateAddress")
//    public ResponseEntity<@NonNull UserResponse> updateUserAddress(
//            @RequestBody UpdateUserAddressRequest updateUserAddressRequestBody
//    ) {
//        UserResponse result = service.updateUserAddress(updateUserAddressRequestBody);
//        return ResponseEntity.ok(result);
//    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping("/products/createTable")
//    public ResponseEntity<@NonNull String> createProductsTable() {
//        String result = service.createProductsTable();
//        return ResponseEntity.ok(result);
//    }
