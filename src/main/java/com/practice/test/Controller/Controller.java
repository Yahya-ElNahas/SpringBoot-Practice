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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class Controller {

    private final IService service;
    private final CookieService cookieService;

    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest createUserRequestBody
    ) {
        UserResponse result = service.createUser(createUserRequestBody);
        return ResponseEntity.ok(ApiResponse.success("User created", result));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest loginRequestBody,
            HttpServletResponse response
    ) {
        AuthTokens tokens = service.login(loginRequestBody);
        cookieService.createRefreshTokenCookie(response, tokens.refreshToken());
        return ResponseEntity.ok(ApiResponse.success(
                "Logged in",
                new AuthResponse(tokens.accessToken()))
        );
    }

    @PostMapping("/auth/refreshToken")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @CookieValue(name = "refresh_token") String refreshToken,
            HttpServletResponse response
    ) {
        AuthTokens tokens = service.refreshAccessToken(refreshToken);
        cookieService.createRefreshTokenCookie(response, tokens.refreshToken());
        return ResponseEntity.ok(ApiResponse.success(
                "Token refreshed",
                new AuthResponse(tokens.accessToken()))
        );
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @CookieValue(name = "refresh_token") String refreshToken
    ) {
        String result = service.logout(refreshToken);
        return ResponseEntity.ok(ApiResponse.success(result, null));
    }

    @GetMapping(value = "/users", params = "email")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(
            @RequestParam(name = "email") String email
    ) {
        UserResponse result = service.getUserByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<AllUsersResponse>> AllUsers(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        AllUsersResponse result = service.getAllUsers(pageNumber, pageSize,  sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/products")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @RequestBody CreateProductRequest createProductRequestBody
    ) {
        ProductResponse result = service.createProduct(createProductRequestBody);
        return ResponseEntity.ok(ApiResponse.success("Product created", result));
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<AllProductsResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        AllProductsResponse result = service.getAllProducts(pageNumber, pageSize,  sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/cart/item")
    public ResponseEntity<ApiResponse<CartResponse>> addProductToCart(
            @RequestBody AddToCartRequest addToCartRequestBody
    ) {
        CartResponse result = service.addProductToCart(addToCartRequestBody);
        return ResponseEntity.ok(ApiResponse.success("Product added to cart", result));
    }
}

//    @PostMapping("/users/updateAddress")
//    public ResponseEntity<UserResponse> updateUserAddress(
//            @RequestBody UpdateUserAddressRequest updateUserAddressRequestBody
//    ) {
//        UserResponse result = service.updateUserAddress(updateUserAddressRequestBody);
//        return ResponseEntity.ok(result);
//    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping("/products/createTable")
//    public ResponseEntity<String> createProductsTable() {
//        String result = service.createProductsTable();
//        return ResponseEntity.ok(result);
//    }