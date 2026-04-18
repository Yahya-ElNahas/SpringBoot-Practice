package com.practice.test.Service;

import com.practice.test.Dtos.Requests.CreateProductRequest;
import com.practice.test.Dtos.Requests.CreateUserRequest;
import com.practice.test.Dtos.Requests.LoginRequest;
import com.practice.test.Dtos.Requests.UpdateUserAddressRequest;
import com.practice.test.Dtos.Responses.AllProductsResponse;
import com.practice.test.Dtos.Responses.AllUsersResponse;
import com.practice.test.Dtos.Responses.AuthResponse;
import com.practice.test.Dtos.Responses.UserResponse;
import com.practice.test.Entities.RefreshToken.RefreshToken;
import com.practice.test.Entities.User.User;
import com.practice.test.Entities.User.UserAddress;
import com.practice.test.Entities.User.UserAddressCK;
import com.practice.test.Infrastructure.Exceptions.*;
import com.practice.test.Infrastructure.Jwt.JwtService;
import com.practice.test.Infrastructure.Jwt.RefreshTokenService;
import com.practice.test.Repositories.ProductRepository;
import com.practice.test.Repositories.UserAddressRepository;
import com.practice.test.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class Service implements IService {

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public UserResponse createUser(CreateUserRequest body) {
        if(userRepository.existsByEmail(body.email)) {
            throw new EmailExistsException();
        }

        User user = new User(
                0,
                body.name,
                body.email,
                passwordEncoder.encode(body.password),
                body.role,
                null
        );
        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), null);
    }

    @Transactional
    public AuthResponse login(LoginRequest body) {
        User user = userRepository.findByEmail(body.email)
                .orElseThrow(IncorrectCredentialsException::new);

        if(!passwordEncoder.matches(body.password, user.getPassword())) {
            throw new IncorrectCredentialsException();
        }

        String accessToken = jwtService.generateToken(body.email, user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return new AuthResponse(accessToken, refreshToken);
    }

    @Transactional
    public String logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RefreshTokenNotProvidedException();
        }

        RefreshToken refreshToken = refreshTokenService.findByToken(authHeader.substring(7));
        refreshToken.setRevoked(true);

        return "Logged out";
    }

    @Transactional
    public String refreshAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RefreshTokenNotProvidedException();
        }

        RefreshToken refreshToken = refreshTokenService.findByToken(authHeader.substring(7));

        if(refreshTokenService.isTokenExpired(refreshToken)) {
            refreshToken.setRevoked(true);
            throw new ExpiredTokenException();
        }

        return jwtService.generateToken(
                refreshToken.getUser().getEmail(),
                refreshToken.getUser().getRole().name()
        );
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), null);
    }

    @Transactional
    public UserResponse updateUserAddress(UpdateUserAddressRequest body) {
        if(userAddressRepository.existsById(new UserAddressCK(body.mobile, body.street))) {
            throw new AddressExistsException();
        }

        UserAddress address = new UserAddress(
                new UserAddressCK(body.mobile, body.street),
                body.city,
                body.state
        );
        UserAddress savedAddress = userAddressRepository.save(address);

        User user = userRepository.findById(body.userId)
                .orElseThrow(UserNotFoundException::new);
        user.setAddress(savedAddress);
        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getAddress()
        );
    }

    public AllUsersResponse getAllUsers() {
        return new AllUsersResponse(userRepository.findAll());
    }

    public String createProductsTable() {
        productRepository.createProductsTable();
        return "Success";
    }

    public String createProduct(CreateProductRequest body) {
        productRepository.createProduct(body.name, body.price, body.stock);
        return "Success";
    }

    public AllProductsResponse getAllProducts() {
        return new AllProductsResponse(productRepository.getAllProducts());
    }
}
