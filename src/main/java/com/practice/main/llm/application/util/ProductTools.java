package com.practice.main.llm.application.util;

import com.practice.main.product.infrastructure.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductTools {

    private final ProductRepository productRepository;

    @Tool(description =
            """
                Get the list of all products with their info.
                Call this when the user asks about product related questions.
                If you will respond with a product, include all its info.
            """
    )
    public String getAllProducts() {
        return productRepository.findAll().stream()
                .map(p -> "id: %s, name: %s, price: %s, stock: %d".formatted(
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getStock()))
                .collect(Collectors.joining("\n"));
    }
}
