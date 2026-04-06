package com.practice.test.Entities;

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
