package com.practice.test.cart.controller;

import com.practice.test.cart.application.CartService;
import com.practice.test.cart.application.dto.request.AddToCartRequest;
import com.practice.test.common.response.ApiResponse;
import com.practice.test.cart.application.dto.response.CartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/item")
    public ResponseEntity<ApiResponse<CartResponse>> addProductToCart(
            @RequestBody AddToCartRequest addToCartRequestBody
    ) {
        CartResponse result = cartService.addProductToCart(addToCartRequestBody);
        return ResponseEntity.ok(ApiResponse.success("Product added to cart", result));
    }
}
