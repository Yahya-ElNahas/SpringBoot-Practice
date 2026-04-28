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

    private UUID orderId;
    private UUID userId;
    private String status;
    private BigDecimal totalPrice;
    private List<ReceiptItem> items;
    private Instant createdAt;

}
