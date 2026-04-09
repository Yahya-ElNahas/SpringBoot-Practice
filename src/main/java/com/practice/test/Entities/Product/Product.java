package com.practice.test.Entities.Product;

import lombok.*;

@AllArgsConstructor
@Getter
@ToString
public class Product {

    private final int id;

    private String name;

    private double price;

    private int stock;
}
