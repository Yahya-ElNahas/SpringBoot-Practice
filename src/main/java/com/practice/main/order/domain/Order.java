package com.practice.main.order.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;

    private int totalItems;

    private BigDecimal totalPrice;

    private OrderStatus status;

    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        this.status = OrderStatus.PENDING;
        this.createdAt = Instant.now();
    }
}
