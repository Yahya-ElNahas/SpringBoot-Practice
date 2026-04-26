package com.practice.test.product.infrastructure;

import com.practice.test.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsProductsByName(String name);
}