package com.practice.main.product.application;

import com.practice.main.product.domain.Product;
import com.practice.main.product.application.dto.ProductMapper;
import com.practice.main.product.infrastructure.ProductRepository;
import com.practice.main.product.application.dto.request.CreateProductRequest;
import com.practice.main.product.application.dto.response.AllProductsResponse;
import com.practice.main.product.application.dto.response.ProductResponse;
import com.practice.main.product.application.exception.ProductNameExistsException;
import com.practice.main.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductResponse createProduct(UserPrincipal userPrincipal, CreateProductRequest body) {
        UUID userId = userPrincipal.userId();

        if(productRepository.existsProductsByName(body.name())) {
            throw new ProductNameExistsException();
        }

        Product product = Product.builder()
                .createdBy(userId)
                .name(body.name())
                .price(BigDecimal.valueOf(body.price()))
                .stock(body.stock())
                .build();
        Product savedProduct = productRepository.save(product);

        return productMapper.toProductResponse(savedProduct);
    }

    public AllProductsResponse getAllProducts(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection
    ) {
        Set<String> ALLOWED_SORT_FIELDS = Set.of("name", "price", "stock");
        if(!ALLOWED_SORT_FIELDS.contains(sortBy.toLowerCase())) {
            sortBy = "name";
        }
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponse> productResponses = productPage.getContent()
                .stream().map(productMapper::toProductResponse).toList();

        return new AllProductsResponse(
                productResponses,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages()
        );
    }
}