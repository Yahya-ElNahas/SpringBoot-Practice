package com.practice.main.dashboard.application.dto.response;

import com.practice.main.cart.application.dto.response.CartItemResponse;
import com.practice.main.order.application.dto.response.OrderResponse;
import com.practice.main.product.application.dto.response.ProductResponse;
import com.practice.main.user.application.dto.response.UserResponse;

import java.util.List;

public record DashboardResponse(
    UserResponse user,
    List<CartItemResponse> cart,
    List<OrderResponse> orders,
    List<ProductResponse> latestProducts
) {}