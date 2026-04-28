package com.practice.test.cart.controller;

import com.practice.test.cart.application.CartService;
import com.practice.test.cart.application.dto.request.AddToCartRequest;
import com.practice.test.cart.application.dto.response.CartItemResponse;
import com.practice.test.common.response.ApiResponse;
import com.practice.test.security.principal.UserPrincipal;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "04 - Cart Controller")
public class CartController {

    private final CartService cartService;

    @PostMapping("/item")
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> addProductToCart(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody AddToCartRequest addToCartRequestBody
    ) {
        List<CartItemResponse> result = cartService.addProductToCart(userPrincipal, addToCartRequestBody);
        return ResponseEntity.ok(ApiResponse.success("Product added to cart", result));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> getCart(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        List<CartItemResponse> result = cartService.getCart(userPrincipal);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}