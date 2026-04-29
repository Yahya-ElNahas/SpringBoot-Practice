package com.practice.main.order.application.dto;

import com.practice.main.cart.domain.CartItem;
import com.practice.main.order.application.dto.internal.OrderItemDto;
import com.practice.main.order.application.dto.response.OrderResponse;
import com.practice.main.order.domain.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "productId", expression = "java(item.getProduct().getId().toString())")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "subTotal", source = "item", qualifiedByName = "calculateSubTotal")
    OrderItemDto toOrderItemResponse(CartItem item);

    @Named("calculateSubTotal")
    default BigDecimal calculateSubTotal(CartItem item) {
        return BigDecimal.valueOf(item.getQuantity())
                .multiply(item.getProduct().getPrice());
    }
}
