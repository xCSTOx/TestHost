package com.example.userregistration.controller;

import com.example.userregistration.entity.User;
import com.example.userregistration.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public static class UserRequest {
        @NotBlank(message = "Username is required")
        @Size(min = 3, message = "Username must be at least 3 characters")
        public String username;

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        public String password;
    }

    public static class UserUpdate {

        @NotBlank(message = "Username is required")
        @Size(min = 3, message = "Username must be at least 3 characters")
        public String username;

    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest request) {
        User newUser = userService.registerUser(request.username, request.password);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered with ID: " + newUser.getUserId());
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody UserRequest request) {
        Optional<User> userOpt = userService.findByUsername(request.username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (BCrypt.checkpw(request.password, user.getPassword())) {
                return "Hello, " + user.getUsername() + "!";
            } else {
                return "Wrong password";
            }
        }
        return "User not found";
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}/update")
    public String updateUsername(@PathVariable String userId, @Valid @RequestBody UserUpdate request) {
        if (userService.existsByUsername(request.username)) {
            return "Username is already taken";
        }
        return userService.updateUsername(userId, request.username);
    }
}
