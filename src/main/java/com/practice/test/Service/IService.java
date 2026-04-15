package com.practice.test.Service;

import com.practice.test.Dtos.Requests.CreateProductRequest;
import com.practice.test.Dtos.Requests.CreateUserRequest;
import com.practice.test.Dtos.Requests.LoginRequest;
import com.practice.test.Dtos.Requests.UpdateUserAddressRequest;
import com.practice.test.Dtos.Responses.AllProductsResponse;
import com.practice.test.Dtos.Responses.AllUsersResponse;
import com.practice.test.Dtos.Responses.AuthResponse;
import com.practice.test.Dtos.Responses.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface IService {

    public UserResponse createUser(CreateUserRequest body);
    public AuthResponse login(LoginRequest body);
    public String logout(HttpServletRequest request);
    public String refreshAccessToken(HttpServletRequest request);

    public UserResponse getUserByEmail(String email);
    public UserResponse updateUserAddress(UpdateUserAddressRequest body);
    public AllUsersResponse getAllUsers();

    public String createProductsTable();
    public String createProduct(CreateProductRequest body);
    public AllProductsResponse getAllProducts();
}