package com.practice.test.Dtos.Responses;

import com.practice.test.Entities.Product.Product;

import java.util.List;

public record AllProductsResponse(List<Product> products) {
}
