package com.practice.test.Repositories;

import com.practice.test.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsProductsByName(String name);
}