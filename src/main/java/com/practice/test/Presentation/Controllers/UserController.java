package com.practice.test.Presentation.Controllers;

import com.practice.test.Application.Dtos.CreateUserRequest;
import com.practice.test.Application.Dtos.UserResponse;
import com.practice.test.Application.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        UserResponse result = userService.createUser(createUserRequest);
        return ResponseEntity.ok(result);
    }

    @GetMapping()
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam(name = "email") String email) {
        UserResponse result = userService.getUserByEmail(email);
        return ResponseEntity.ok(result);
    }
}