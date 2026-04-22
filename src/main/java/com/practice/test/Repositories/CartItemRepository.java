package com.practice.test.Repositories;

import com.practice.test.Entities.CartItem;
import com.practice.test.Entities.Product;
import com.practice.test.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    Optional<CartItem> findCartItemByUserIdAndProductId(UUID userId, UUID productId);
    List<CartItem> findAllByUserId(UUID userId);
}