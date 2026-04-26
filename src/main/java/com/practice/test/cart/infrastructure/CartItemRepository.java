package com.practice.test.cart.infrastructure;

import com.practice.test.cart.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    Optional<CartItem> findCartItemByUserIdAndProductId(UUID userId, UUID productId);
    List<CartItem> findAllByUserId(UUID userId);
}