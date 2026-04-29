package com.practice.main.cart.infrastructure;

import com.practice.main.cart.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    @Query("""
            SELECT cart FROM CartItem cart JOIN FETCH cart.product
            WHERE cart.userId = :userId and cart.product.id = :productId
    """)
    Optional<CartItem> findCartItemByUserIdAndProductId(UUID userId, UUID productId);

    @Query("""
            SELECT cart FROM CartItem cart JOIN FETCH cart.product
            WHERE cart.userId = :userId ORDER BY cart.addedAt ASC
    """)
    List<CartItem> findAllByUserId(UUID userId);

    @Query("""
            DELETE FROM CartItem 
            WHERE userId = :userId
            """)
    @Modifying
    void deleteAllByUserId(UUID userId);
}