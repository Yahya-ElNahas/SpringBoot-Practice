package com.practice.test.product.controller;

import com.practice.test.product.application.ProductService;
import com.practice.test.product.application.dto.request.CreateProductRequest;
import com.practice.test.product.application.dto.response.AllProductsResponse;
import com.practice.test.common.response.ApiResponse;
import com.practice.test.product.application.dto.response.ProductResponse;
import com.practice.test.security.principal.UserPrincipal;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "03 - Product Controller")
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreateProductRequest createProductRequestBody
    ) {
        ProductResponse result = productService.createProduct(userPrincipal, createProductRequestBody);
        return ResponseEntity.ok(ApiResponse.success("Product created", result));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<AllProductsResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        AllProductsResponse result = productService.getAllProducts(pageNumber, pageSize,  sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
