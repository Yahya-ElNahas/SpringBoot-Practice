package com.practice.main.cart.application;

import com.practice.main.cart.application.exception.CartItemProductNotFoundException;
import com.practice.main.cart.domain.CartItem;
import com.practice.main.cart.infrastructure.CartItemRepository;
import com.practice.main.cart.application.dto.CartMapper;
import com.practice.main.cart.application.dto.response.CartItemResponse;
import com.practice.main.cart.application.dto.request.AddToCartRequest;
import com.practice.main.cart.application.exception.InsufficientStockException;
import com.practice.main.security.principal.UserPrincipal;
import com.practice.main.product.domain.Product;
import com.practice.main.product.infrastructure.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    private final CartMapper cartMapper;

    @Transactional
    public List<CartItemResponse> addProductToCart(UserPrincipal userPrincipal, AddToCartRequest body) {
        UUID userId = userPrincipal.userId();

        Product product = productRepository.findByIdForUpdate(body.productId())
                .orElseThrow(() -> new CartItemProductNotFoundException(body.productId()));

        Optional<CartItem> existingCartItem = cartItemRepository.findCartItemByUserIdAndProductId(
                userId,
                product.getId()
        );

        int totalQuantity = existingCartItem.map(
                cartItem -> cartItem.getQuantity() + body.quantity()
                )
                .orElse(body.quantity());

        if(totalQuantity > product.getStock()) {
            throw new InsufficientStockException(product.getStock());
        }

        CartItem cartItem = existingCartItem.orElseGet(() ->
                CartItem.builder()
                .userId(userId)
                .product(product)
                .build()
        );
        cartItem.setQuantity(totalQuantity);
        cartItemRepository.save(cartItem);

        List<CartItem> cart = cartItemRepository.findAllByUserId(userId);

        return cartMapper.toCartItemListResponse(cart);
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> getCart(UserPrincipal userPrincipal) {
        UUID userId = userPrincipal.userId();

        List<CartItem> cart = cartItemRepository.findAllByUserId(userId);

        return cartMapper.toCartItemListResponse(cart);
    }
}