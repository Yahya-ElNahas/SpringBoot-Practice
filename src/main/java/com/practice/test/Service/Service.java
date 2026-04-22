package com.practice.test.Service;

import com.practice.test.Dtos.DtoMapper;
import com.practice.test.Dtos.Internal.AuthTokens;
import com.practice.test.Dtos.Internal.CartItemResponse;
import com.practice.test.Dtos.Requests.AddToCartRequest;
import com.practice.test.Dtos.Requests.CreateProductRequest;
import com.practice.test.Dtos.Requests.CreateUserRequest;
import com.practice.test.Dtos.Requests.LoginRequest;
import com.practice.test.Dtos.Responses.*;
import com.practice.test.Entities.CartItem;
import com.practice.test.Entities.Product;
import com.practice.test.Entities.Session;
import com.practice.test.Entities.User;
import com.practice.test.Infrastructure.Exceptions.*;
import com.practice.test.Infrastructure.Jwt.JwtService;
import com.practice.test.Infrastructure.Jwt.JwtUserPrincipal;
import com.practice.test.Repositories.CartItemRepository;
import com.practice.test.Repositories.ProductRepository;
import com.practice.test.Repositories.SessionRepository;
import com.practice.test.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class Service implements IService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SessionRepository sessionRepository;
    private final CartItemRepository cartItemRepository;

    private final JwtService jwtService;
    private final TokenService tokenService;

    private final DtoMapper dtoMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(CreateUserRequest body) {
        if(userRepository.existsByEmail(body.email())) {
            throw new EmailExistsException();
        }

        User user = User.builder()
                .name(body.name())
                .email(body.email())
                .password(passwordEncoder.encode(body.password()))
                .role(body.role())
                .build();
        User savedUser = userRepository.save(user);

        return dtoMapper.toUserResponse(savedUser);
    }

    @Transactional
    public AuthTokens login(LoginRequest body) {
        User user = userRepository.findByEmail(body.email())
                .orElseThrow(IncorrectCredentialsException::new);

        if(!passwordEncoder.matches(body.password(), user.getPassword())) {
            throw new IncorrectCredentialsException();
        }

        String refreshToken = tokenService.generateRefreshToken();

        Session session = Session.builder()
                .user(user)
                .token(tokenService.hash(refreshToken))
                .expiryDate(LocalDateTime.now().plusHours(1))
                .build();
        Session savedSession = sessionRepository.save(session);

        String accessToken = jwtService.generateToken(
                user.getId(),
                user.getRole().name(),
                savedSession.getId()
        );

        return new AuthTokens(accessToken, refreshToken);
    }

    @Transactional
    public AuthTokens refreshAccessToken(String refreshToken) {
        String hashedToken = tokenService.hash(refreshToken);

        Session session = sessionRepository.findByToken(hashedToken)
                .orElseThrow(InvalidSessionException::new);

        if(session.isRevoked()
                || session.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidSessionException();
        }

        User user = session.getUser();

        String newRefreshToken = tokenService.generateRefreshToken();

        session.setToken(tokenService.hash(newRefreshToken));
        session.setExpiryDate(LocalDateTime.now().plusHours(1));
        Session savedSession = sessionRepository.save(session);

        String accessToken = jwtService.generateToken(
                user.getId(),
                user.getRole().name(),
                savedSession.getId()
        );

        return new AuthTokens(accessToken, newRefreshToken);
    }

    @Transactional
    public String logout(String refreshToken) {
        String hashedToken = tokenService.hash(refreshToken);

        Session session = sessionRepository.findByToken(hashedToken)
                .orElseThrow(InvalidSessionException::new);
        session.setRevoked(true);
        sessionRepository.save(session);

        return "Logged out";
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        return dtoMapper.toUserResponse(user);
    }

    public AllUsersResponse getAllUsers(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection
    ) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> usersPage = userRepository.findAll(pageable);

        List<UserResponse> userResponses = usersPage.getContent()
                .stream().map(dtoMapper::toUserResponse
                ).toList();

        return new AllUsersResponse(
                userResponses,
                usersPage.getNumber(),
                usersPage.getSize(),
                usersPage.getTotalElements(),
                usersPage.getTotalPages()
        );
    }

    public ProductResponse createProduct(CreateProductRequest body) {
        JwtUserPrincipal principal = (JwtUserPrincipal)
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

        return dtoMapper.toProductResponse(savedProduct);
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

    @Transactional
    public CartResponse addProductToCart(AddToCartRequest body) {
        JwtUserPrincipal principal = (JwtUserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UUID userId = principal.userId();

        Product product = productRepository.findById(body.productId())
                .orElseThrow(ProductNotFoundException::new);

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

        List<CartItemResponse> cart = dtoMapper.toCartItemListResponse(cartItemRepository.findAllByUserId(userId));

        return new CartResponse(cart);
    }
}

//    private final UserAddressRepository userAddressRepository;

//    @Transactional
//    public UserResponse updateUserAddress(UpdateUserAddressRequest body) {
//        if(userAddressRepository.existsById(new UserAddressCK(body.mobile, body.street))) {
//            throw new AddressExistsException();
//        }
//
//        UserAddress address = new UserAddress(
//                new UserAddressCK(body.mobile, body.street),
//                body.city,
//                body.state
//        );
//        UserAddress savedAddress = userAddressRepository.save(address);
//
//        User user = userRepository.findById(body.userId)
//                .orElseThrow(UserNotFoundException::new);
//        user.setAddress(savedAddress);
//        User savedUser = userRepository.save(user);
//
//        return new UserResponse(
//                savedUser.getId(),
//                savedUser.getName(),
//                savedUser.getEmail(),
//                savedUser.getAddress()
//        );
//    }

//    public String createProductsTable() {
//        productRepository.createProductsTable();
//        return "Success";
//    }