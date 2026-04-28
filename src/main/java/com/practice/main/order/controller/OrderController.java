package com.practice.main.order.controller;

import com.practice.main.common.response.ApiResponse;
import com.practice.main.order.application.OrderService;
import com.practice.main.order.application.dto.response.OrderResponse;
import com.practice.main.security.principal.UserPrincipal;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
@Tag(name = "04 - Order Controller")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        OrderResponse result = orderService.placeOrder(userPrincipal);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
