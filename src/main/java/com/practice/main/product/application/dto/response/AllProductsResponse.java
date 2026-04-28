package com.practice.main.product.application.dto.response;

import java.util.List;

public record AllProductsResponse(
        List<ProductResponse> products,
        int page,
        int size,
        long totalProducts,
        int totalPages
) {}