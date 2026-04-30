package com.practice.main.receipt.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReceiptDocument {

    private String orderId;
    private String userId;
    private List<ReceiptItem> items;
    private double totalPrice;
    private String status;
    private Instant createdAt;
}