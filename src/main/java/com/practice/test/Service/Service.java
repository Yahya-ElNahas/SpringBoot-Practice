package com.practice.test.Service;

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

@org.springframework.stereotype.Service
@AllArgsConstructor
public class Service implements IService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SessionRepository sessionRepository;
    private final CartItemRepository cartItemRepository;

    private final JwtService jwtService;
    private final TokenService tokenService;

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

        return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), null);
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
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCart());
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

        List<UserResponse> userResponse = usersPage.getContent()
                .stream().map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getCart()
                )).toList();

        return new AllUsersResponse(
                userResponse,
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

        return new ProductResponse(
                savedProduct.getId(),
                savedProduct.getCreatedBy().getId(),
                savedProduct.getName(),
                savedProduct.getPrice(),
                savedProduct.getStock()
        );
    }

    public AllProductsResponse getAllProducts() {
        return new AllProductsResponse(productRepository.findAll());
    }

    @Transactional
    public CartResponse addProductToCart(AddToCartRequest body) {
        JwtUserPrincipal principal = (JwtUserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findById(principal.userId())
                .orElseThrow(UserNotFoundException::new);

        Product product = productRepository.findById(body.productId())
                .orElseThrow(ProductNotFoundException::new);

        if(body.quantity() > product.getStock()) {
            throw new InsufficientStockException(product.getStock());
        }

        CartItem cartItem = CartItem.builder()
                .user(user)
                .product(product)
                .quantity(body.quantity())
                .build();
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        user.addToCart(savedCartItem);
        User savedUser = userRepository.save(user);

        return new CartResponse(
                savedUser.getCart().stream().map(
                        item -> new CartItemResponse(
                            item.getId(),
                            item.getProduct(),
                            item.getQuantity()
                        )
                ).toList()
        );
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