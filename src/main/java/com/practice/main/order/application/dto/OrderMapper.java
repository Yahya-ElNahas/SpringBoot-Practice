package com.practice.main.order.application.dto;

import com.practice.main.order.application.dto.response.OrderResponse;
import com.practice.main.order.domain.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toOrderResponse(Order order);
}
