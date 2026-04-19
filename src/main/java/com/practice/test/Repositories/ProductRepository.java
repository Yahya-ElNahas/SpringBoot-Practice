package com.practice.test.Repositories;

import com.practice.test.Entities.Product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}