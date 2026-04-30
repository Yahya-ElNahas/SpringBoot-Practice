package com.practice.main.dashboard.application;

import com.practice.main.cart.application.CartService;
import com.practice.main.cart.application.dto.response.CartItemResponse;
import com.practice.main.dashboard.application.dto.DashboardResponse;
import com.practice.main.order.application.OrderService;
import com.practice.main.order.application.dto.response.OrderResponse;
import com.practice.main.product.application.ProductService;
import com.practice.main.product.application.dto.response.ProductResponse;
import com.practice.main.security.principal.UserPrincipal;
import com.practice.main.user.application.UserService;
import com.practice.main.user.application.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserService userService;
    private final ProductService productService;
    private final CartService cartService;
    private final OrderService orderService;

    public DashboardResponse getUserDashboard(UserPrincipal principal) {
        CompletableFuture<UserResponse> getUserTask = userService.getUserAsync(principal.userId());
        CompletableFuture<List<ProductResponse>> getProductsTask = productService.getLatestProducts(3);
        CompletableFuture<List<CartItemResponse>> getCartTask = cartService.getUserCartAsync(principal.userId());
        CompletableFuture<List<OrderResponse>> getOrdersTask = orderService.getUserOrdersAsync(principal.userId());

        CompletableFuture.allOf(
                getUserTask,
                getProductsTask,
                getCartTask,
                getOrdersTask
        ).join();

        return new DashboardResponse(
                getUserTask.join(),
                getCartTask.join(),
                getOrdersTask.join(),
                getProductsTask.join()
        );
    }
}
