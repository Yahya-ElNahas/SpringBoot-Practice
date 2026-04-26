package com.practice.test.authentication.controller;

import com.practice.test.authentication.application.AuthService;
import com.practice.test.common.exception.InvalidTokenException;
import com.practice.test.authentication.application.dto.response.AuthTokens;
import com.practice.test.user.application.dto.request.CreateUserRequest;
import com.practice.test.authentication.application.dto.request.LoginRequest;
import com.practice.test.common.response.ApiResponse;
import com.practice.test.authentication.application.dto.response.AuthResponse;
import com.practice.test.user.application.dto.response.UserResponse;
import com.practice.test.security.cookie.CookieService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieService cookieService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest createUserRequestBody
    ) {
        UserResponse result = authService.createUser(createUserRequestBody);
        return ResponseEntity.ok(ApiResponse.success("User created", result));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest loginRequestBody,
            HttpServletResponse response
    ) {
        AuthTokens tokens = authService.login(loginRequestBody);
        cookieService.createRefreshTokenCookie(response, tokens.refreshToken());
        return ResponseEntity.ok(ApiResponse.success(
                "Logged in",
                new AuthResponse(tokens.accessToken()))
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @CookieValue(name = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken == null) {
            throw new InvalidTokenException();
        }
        AuthTokens tokens = authService.refreshAccessToken(refreshToken);
        cookieService.createRefreshTokenCookie(response, tokens.refreshToken());
        return ResponseEntity.ok(ApiResponse.success(
                "Token refreshed",
                new AuthResponse(tokens.accessToken()))
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @CookieValue(name = "refresh_token", required = false) String refreshToken
    ) {
        if(refreshToken != null) {
            authService.logout(refreshToken);
        }
        return ResponseEntity.ok(ApiResponse.success("Logged out", null));
    }
}
