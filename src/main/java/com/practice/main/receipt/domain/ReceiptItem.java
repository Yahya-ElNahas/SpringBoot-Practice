package com.practice.main.receipt.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReceiptItem {

    private UUID productId;
    private String productName;
    private BigDecimal productPrice;
    private int quantity;
    private BigDecimal subTotal;
}
