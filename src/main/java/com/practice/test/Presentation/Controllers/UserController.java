package com.practice.test.Presentation.Controllers;

import com.practice.test.Application.Dtos.CreateUserRequest;
import com.practice.test.Application.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody CreateUserRequest createUserRequest) {
        try {
            String result = userService.createUser(createUserRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
