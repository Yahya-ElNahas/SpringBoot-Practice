package com.practice.main.cart.application.dto;

import com.practice.main.cart.domain.CartItem;
import com.practice.main.cart.application.dto.response.CartItemResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartItemResponse toCartItemResponse(CartItem cartItem);

    List<CartItemResponse> toCartItemListResponse(List<CartItem> cartItems);
}