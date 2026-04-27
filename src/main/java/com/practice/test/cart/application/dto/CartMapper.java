package com.practice.test.cart.application.dto;

import com.practice.test.cart.domain.CartItem;
import com.practice.test.cart.application.dto.response.CartItemResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartItemResponse toCartItemResponse(CartItem cartItem);

    List<CartItemResponse> toCartItemListResponse(List<CartItem> cartItems);
}