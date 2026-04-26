package com.practice.test.product.application.dto;

import com.practice.test.product.domain.Product;
import com.practice.test.product.application.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "createdBy", source = "createdBy.id")
    ProductResponse toProductResponse(Product product);
}
