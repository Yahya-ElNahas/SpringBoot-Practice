package com.practice.test.Dtos;

import com.practice.test.Dtos.Internal.CartItemResponse;
import com.practice.test.Dtos.Responses.*;
import com.practice.test.Entities.CartItem;
import com.practice.test.Entities.Product;
import com.practice.test.Entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    UserResponse toUserResponse(User user);

    @Mapping(target = "createdBy", source = "createdBy.id")
    ProductResponse toProductResponse(Product product);

    CartItemResponse toCartItemResponse(CartItem cartItem);
    List<CartItemResponse> toCartItemListResponse(List<CartItem> cartItems);
}