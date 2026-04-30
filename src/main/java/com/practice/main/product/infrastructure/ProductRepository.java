package com.practice.main.product.infrastructure;

import com.practice.main.product.domain.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsProductsByName(String name);

    @Query("""
        SELECT p FROM Product p
        WHERE p.id = :id
    """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findByIdForUpdate(UUID id);

    @Query("""
        UPDATE Product
        SET stock = stock - :by
        WHERE id = :id AND stock >= :by
    """)
    @Modifying
    int decrementStock(UUID id, int by);

    @Query("""
        SELECT p FROM Product p
        ORDER BY p.createdAt DESC
    """)
    List<Product> findLatestProducts(Pageable pageable);
}