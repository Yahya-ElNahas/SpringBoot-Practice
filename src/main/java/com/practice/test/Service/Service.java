package com.practice.test.Service;

import com.practice.test.Dtos.Internal.AuthTokens;
import com.practice.test.Dtos.Requests.AddToCartRequest;
import com.practice.test.Dtos.Requests.CreateProductRequest;
import com.practice.test.Dtos.Requests.CreateUserRequest;
import com.practice.test.Dtos.Requests.LoginRequest;
import com.practice.test.Dtos.Responses.*;
import com.practice.test.Entities.Product.Product;
import com.practice.test.Entities.Session.Session;
import com.practice.test.Entities.User.User;
import com.practice.test.Infrastructure.Exceptions.*;
import com.practice.test.Infrastructure.Jwt.JwtService;
import com.practice.test.Repositories.ProductRepository;
import com.practice.test.Repositories.SessionRepository;
import com.practice.test.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class Service implements IService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SessionRepository sessionRepository;
    private final TokenService tokenService;

    @Transactional
    public UserResponse createUser(CreateUserRequest body) {
        if(userRepository.existsByEmail(body.email())) {
            throw new EmailExistsException();
        }

        User user = new User(
                0,
                body.name(),
                body.email(),
                passwordEncoder.encode(body.password()),
                body.role(),
                null
        );
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

        Session session = new Session(
                0,
                user,
                tokenService.hash(refreshToken),
                LocalDateTime.now().plusMinutes(5),
                false
        );
        Session savedSession = sessionRepository.save(session);

        String accessToken = jwtService.generateToken(user.getEmail(), user.getRole().name(), savedSession.getId());

        return new AuthTokens(accessToken, refreshToken);
    }

    @Transactional
    public AuthTokens refreshAccessToken(String refreshToken) {
        String hashedToken = tokenService.hash(refreshToken);

        Session session = sessionRepository.findByToken(hashedToken)
                .orElseThrow(InvalidRefreshTokenException::new);

        if(session.isRevoked()
                || session.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidRefreshTokenException();
        }

        User user = session.getUser();

        String newRefreshToken = tokenService.generateRefreshToken();

        session.setToken(tokenService.hash(newRefreshToken));
        session.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        Session savedSession = sessionRepository.save(session);

        String accessToken = jwtService.generateToken(user.getEmail(), user.getRole().name(), savedSession.getId());

        return new AuthTokens(accessToken, newRefreshToken);
    }

    @Transactional
    public String logout(String refreshToken) {
        String hashedToken = tokenService.hash(refreshToken);

        Session session = sessionRepository.findByToken(hashedToken)
                .orElseThrow(InvalidRefreshTokenException::new);
        session.setRevoked(true);
        sessionRepository.save(session);

        return "Logged out";
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCart());
    }

    public AllUsersResponse getAllUsers() {
        return new AllUsersResponse(userRepository.findAll());
    }

    public ProductResponse createProduct(CreateProductRequest body) {
        Product product = new Product(
                0,
                body.name(),
                body.price(),
                body.stock()
        );
        Product savedProduct = productRepository.save(product);

        return new ProductResponse(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getPrice(),
                savedProduct.getStock()
        );
    }

    public AllProductsResponse getAllProducts() {
        return new AllProductsResponse(productRepository.findAll());
    }

    public CartResponse addProductToCart(AddToCartRequest body) {
        Product product = productRepository.findById(body.productId())
                .orElseThrow(ProductNotFoundException::new);


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
