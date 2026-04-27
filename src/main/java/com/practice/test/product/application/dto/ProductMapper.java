package com.practice.test.product.application.dto;

import com.practice.test.product.domain.Product;
import com.practice.test.product.application.dto.response.ProductResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toProductResponse(Product product);
}
