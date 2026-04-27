package com.practice.test.authentication.controller;

import com.practice.test.authentication.application.AuthService;
import com.practice.test.authentication.application.exception.InvalidTokenException;
import com.practice.test.authentication.application.dto.response.AuthTokens;
import com.practice.test.user.application.dto.request.CreateUserRequest;
import com.practice.test.authentication.application.dto.request.LoginRequest;
import com.practice.test.common.response.ApiResponse;
import com.practice.test.authentication.application.dto.response.AuthResponse;
import com.practice.test.user.application.dto.response.UserResponse;
import com.practice.test.security.cookie.CookieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "01 - Authentication Controller")
public class AuthController {

    private final AuthService authService;
    private final CookieService cookieService;

    @Operation(summary = "Create user")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest createUserRequestBody
    ) {
        UserResponse result = authService.createUser(createUserRequestBody);
        return ResponseEntity.ok(ApiResponse.success("User created", result));
    }

    @Operation(summary = """
            Login user with email and password.
            Refresh and access tokens are generated upon successful login and session is created
            """)
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

    @Operation(summary = """
            Generate new refresh and access tokens and create new session
            """)
    @PostMapping("/refresh-token")
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

    @Operation(summary = """
            Logout user by invalidating the current session
            """)
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
