package com.practice.test.product.application;

import com.practice.test.product.domain.Product;
import com.practice.test.product.application.dto.ProductMapper;
import com.practice.test.product.infrastructure.ProductRepository;
import com.practice.test.product.application.dto.request.CreateProductRequest;
import com.practice.test.product.application.dto.response.AllProductsResponse;
import com.practice.test.product.application.dto.response.ProductResponse;
import com.practice.test.common.exception.ProductNameExistsException;
import com.practice.test.common.exception.UserNotFoundException;
import com.practice.test.security.principal.UserPrincipal;
import com.practice.test.user.domain.User;
import com.practice.test.user.Infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final ProductMapper productMapper;

    public ProductResponse createProduct(CreateProductRequest body) {
        UserPrincipal principal = (UserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findById(principal.userId())
                .orElseThrow(UserNotFoundException::new);

        if(productRepository.existsProductsByName(body.name())) {
            throw new ProductNameExistsException();
        }

        Product product = Product.builder()
                .createdBy(user)
                .name(body.name())
                .price(body.price())
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
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponse> productResponses = productPage.getContent()
                .stream().map(product -> new ProductResponse(
                        product.getId(),
                        product.getCreatedBy().getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getStock()
                )).toList();

        return new AllProductsResponse(
                productResponses,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages()
        );
    }
}
