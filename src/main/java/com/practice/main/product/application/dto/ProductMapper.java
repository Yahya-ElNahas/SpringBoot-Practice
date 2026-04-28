package com.practice.main.product.application.dto;

import com.practice.main.product.domain.Product;
import com.practice.main.product.application.dto.response.ProductResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toProductResponse(Product product);
}
