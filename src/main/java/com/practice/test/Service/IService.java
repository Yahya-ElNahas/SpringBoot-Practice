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

    UserResponse createUser(CreateUserRequest body);
    AuthResponse login(LoginRequest body);
    String logout(HttpServletRequest request);
    String refreshAccessToken(HttpServletRequest request);

    UserResponse getUserByEmail(String email);
    UserResponse updateUserAddress(UpdateUserAddressRequest body);
    AllUsersResponse getAllUsers();

    String createProductsTable();
    String createProduct(CreateProductRequest body);
    AllProductsResponse getAllProducts();
}