package com.practice.main.order.application;

import com.practice.main.cart.domain.CartItem;
import com.practice.main.cart.infrastructure.CartItemRepository;
import com.practice.main.order.application.dto.OrderMapper;
import com.practice.main.order.application.dto.response.OrderResponse;
import com.practice.main.order.application.exception.EmptyCartExcpetion;
import com.practice.main.order.domain.Order;
import com.practice.main.order.domain.OrderItem;
import com.practice.main.order.infrastructure.OrderRepository;
import com.practice.main.product.domain.Product;
import com.practice.main.receipt.application.ReceiptService;
import com.practice.main.receipt.domain.ReceiptDocument;
import com.practice.main.receipt.domain.ReceiptItem;
import com.practice.main.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartRepository;

    private final ReceiptService receiptService;

    private final OrderMapper mapper;

    @Transactional
    public OrderResponse placeOrder(UserPrincipal userPrincipal) {
        UUID userId = userPrincipal.userId();

        List<CartItem> cart = cartRepository.findAllByUserId(userId);
        if(cart.isEmpty()) {
            throw new EmptyCartExcpetion();
        }

        BigDecimal totalPrice = BigDecimal.ZERO;

        for(CartItem cartItem : cart) {
            totalPrice = totalPrice.add(
                    cartItem.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
            );
        }

        List<OrderItem> orderItems = cart.stream().map(
                cartItem -> {
                    Product product = cartItem.getProduct();

                    // decrement stock

                    return OrderItem.builder()
                            .productId(product.getId())
                            .productName(product.getName())
                            .quantity(cartItem.getQuantity())
                            .price(product.getPrice())
                            .subTotal(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                            .build();
                }
        ).toList();

        Order order = Order.builder()
                .userId(userId)
                .items(orderItems)
                .totalPrice(totalPrice)
                .build();
        Order savedOrder = orderRepository.save(order);

        List<ReceiptItem> receiptItems = cart.stream().map(
                cartItem -> {
                    Product product = cartItem.getProduct();

                    return ReceiptItem.builder()
                            .productId(product.getId())
                            .productName(product.getName())
                            .quantity(cartItem.getQuantity())
                            .productPrice(product.getPrice())
                            .subTotal(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                            .build();
                }
        ).toList();

        ReceiptDocument receipt = ReceiptDocument.builder()
                .orderId(savedOrder.getId())
                .userId(savedOrder.getUserId())
                .items(receiptItems)
                .totalPrice(savedOrder.getTotalPrice())
                .status("PENDING") // make it enum
                .createdAt(savedOrder.getCreatedAt())
                .build();

        receiptService.saveReceipt(receipt);

        cartRepository.deleteAllByUserId(userId);

        return mapper.toOrderResponse(savedOrder);
    }
}