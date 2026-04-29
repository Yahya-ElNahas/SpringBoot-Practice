package com.practice.main.receipt.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class ReceiptDocument {

    private UUID orderId;
    private UUID userId;
    private List<ReceiptItem> items;
    private BigDecimal totalPrice;
    private String status;
    private Instant createdAt;
}