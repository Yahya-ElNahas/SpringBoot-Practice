package com.practice.test.cart.application;

import com.practice.test.cart.application.exception.CartItemProductNotFoundException;
import com.practice.test.cart.domain.CartItem;
import com.practice.test.cart.infrastructure.CartItemRepository;
import com.practice.test.cart.application.dto.CartMapper;
import com.practice.test.cart.application.dto.response.CartItemResponse;
import com.practice.test.cart.application.dto.request.AddToCartRequest;
import com.practice.test.cart.application.dto.response.CartResponse;
import com.practice.test.cart.application.exception.InsufficientStockException;
import com.practice.test.security.principal.UserPrincipal;
import com.practice.test.product.domain.Product;
import com.practice.test.product.infrastructure.ProductRepository;
import com.practice.test.user.Infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final CartMapper cartMapper;

    @Transactional
    public CartResponse addProductToCart(UserPrincipal userPrincipal, AddToCartRequest body) {
        UUID userId = userPrincipal.userId();

        Product product = productRepository.findById(body.productId())
                .orElseThrow(() -> new CartItemProductNotFoundException(body.productId()));

        CartItem cartItem = cartItemRepository.findCartItemByUserIdAndProductId(userId, product.getId())
                .orElse(null);

        int totalQuantity = cartItem != null ?
                body.quantity() + cartItem.getQuantity() :
                body.quantity();

        if(totalQuantity > product.getStock()) {
            throw new InsufficientStockException(product.getStock());
        }

        if(cartItem != null) {
            cartItem.setQuantity(totalQuantity);
        } else {
            cartItem = CartItem.builder()
                    .user(userRepository.getReferenceById(userId))
                    .product(product)
                    .quantity(totalQuantity)
                    .build();
        }
        cartItemRepository.save(cartItem);

        List<CartItem> cart = cartItemRepository.findAllByUserId(userId);

        List<CartItemResponse> cartResponse = cartMapper.toCartItemListResponse(cart);

        return new CartResponse(cartResponse);
    }

    public CartResponse getCart(UserPrincipal userPrincipal) {
        UUID userId = userPrincipal.userId();

        List<CartItem> cart = cartItemRepository.findAllByUserId(userId);

        List<CartItemResponse> cartResponse = cartMapper.toCartItemListResponse(cart);

        return new CartResponse(cartResponse);
    }
}