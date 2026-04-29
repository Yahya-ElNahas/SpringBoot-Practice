package com.practice.main.order.application;

import com.practice.main.cart.domain.CartItem;
import com.practice.main.cart.infrastructure.CartItemRepository;
import com.practice.main.order.application.dto.OrderMapper;
import com.practice.main.order.application.dto.internal.OrderItemDto;
import com.practice.main.order.application.dto.response.OrderResponse;
import com.practice.main.order.application.dto.internal.OrderPlacedEvent;
import com.practice.main.order.application.exception.EmptyCartExcpetion;
import com.practice.main.order.application.exception.StockUpdateException;
import com.practice.main.order.domain.Order;
import com.practice.main.order.infrastructure.OrderRepository;
import com.practice.main.product.domain.Product;
import com.practice.main.product.infrastructure.ProductRepository;
import com.practice.main.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartRepository;
    private final ProductRepository productRepository;

    private final OrderMapper mapper;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public OrderResponse placeOrder(UserPrincipal userPrincipal) {
        UUID userId = userPrincipal.userId();

        List<CartItem> cart = cartRepository.findAllByUserId(userId);
        if(cart.isEmpty()) {
            throw new EmptyCartExcpetion();
        }

        List<OrderItemDto> orderItems = new ArrayList<>();

        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalItems = 0;

        for(CartItem cartItem : cart) {
            Product product = cartItem.getProduct();

            int updatedStock = productRepository.decrementStock(product.getId(), cartItem.getQuantity());
            if(updatedStock == 0) {
                throw new StockUpdateException();
            }

            BigDecimal subTotal = BigDecimal.valueOf(cartItem.getQuantity())
                    .multiply(product.getPrice());
            totalItems += cartItem.getQuantity();

            orderItems.add(mapper.toOrderItemResponse(cartItem));

            totalPrice = totalPrice.add(subTotal);
        }

        Order order = Order.builder()
                .userId(userId)
                .totalItems(totalItems)
                .totalPrice(totalPrice)
                .build();
        Order savedOrder = orderRepository.save(order);

        eventPublisher.publishEvent(new OrderPlacedEvent(
                savedOrder.getId(),
                savedOrder.getUserId(),
                orderItems,
                totalPrice
        ));

        cartRepository.deleteAllByUserId(userId);

        return mapper.toOrderResponse(savedOrder);
    }
}