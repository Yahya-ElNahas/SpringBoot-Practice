package com.practice.main.order.infrastructure;

import com.practice.main.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findAllByCreatedAtBetween(Instant start, Instant end);

    List<Order> findAllByUserId(UUID userId);
}