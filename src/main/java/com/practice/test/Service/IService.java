package com.practice.test.Service;

import com.practice.test.Dtos.Internal.AuthTokens;
import com.practice.test.Dtos.Requests.*;
import com.practice.test.Dtos.Responses.*;

public interface IService {

    UserResponse createUser(CreateUserRequest body);
    AuthTokens login(LoginRequest body);
    AuthTokens refreshAccessToken(String refreshToken);
    String logout(String refreshToken);

    UserResponse getUserByEmail(String email);
    AllUsersResponse getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDirection);

    ProductResponse createProduct(CreateProductRequest body);
    AllProductsResponse getAllProducts();

    CartResponse addProductToCart(AddToCartRequest body);
}

//    UserResponse updateUserAddress(UpdateUserAddressRequest body);
//    String createProductsTable();