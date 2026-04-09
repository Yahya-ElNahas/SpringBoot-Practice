package com.practice.test.Dtos.Requests;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateUserAddressRequest {
    public final int userId;
    public final String mobile;
    public final String street;
    public final String city;
    public final String state;
}
