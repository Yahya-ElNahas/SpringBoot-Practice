package com.practice.main.receipt.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReceiptItem {

    private String productId;
    private String name;
    private double price;
    private int quantity;
    private double subTotal;
}