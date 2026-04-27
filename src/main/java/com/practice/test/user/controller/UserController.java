package com.practice.test.user.controller;

import com.practice.test.user.application.UserService;
import com.practice.test.user.application.dto.response.AllUsersResponse;
import com.practice.test.common.response.ApiResponse;
import com.practice.test.user.application.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "02 - User Controller")
public class UserController {

    private final UserService userService;

    @GetMapping(path = "by-email", params = "email")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(
            @RequestParam(name = "email") String email
    ) {
        UserResponse result = userService.getUserByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<AllUsersResponse>> AllUsers(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        AllUsersResponse result = userService.getAllUsers(pageNumber, pageSize,  sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
