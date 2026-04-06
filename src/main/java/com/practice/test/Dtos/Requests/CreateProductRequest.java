package com.practice.test.Dtos.Requests;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateProductRequest {
    public final String name;
    public final double price;
    public final int stock;
}
