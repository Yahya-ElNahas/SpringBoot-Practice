package com.practice.main.receipt.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
public class ReceiptItem {

    private UUID productId;
    private String name;
    private BigDecimal price;
    private int quantity;
    private BigDecimal subTotal;
}