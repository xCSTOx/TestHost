package com.example.breakfreeBE.userRegistration.controller;

import com.example.breakfreeBE.common.MetaResponse;
import com.example.breakfreeBE.userRegistration.entity.User;
import com.example.breakfreeBE.common.BaseResponse;
import com.example.breakfreeBE.userRegistration.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCrypt;

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

        public String userId;

        @NotBlank(message = "Username is required")
        @Size(min = 3, message = "Username must be at least 3 characters")
        public String username;
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<User>>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(new BaseResponse<>(new MetaResponse(true, "User list retrieved successfully"), users));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new BaseResponse<>(new MetaResponse(false, "Failed to retrieve users    : " + e.getMessage()), null)
            );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<Map<String, String>>> registerUser(@RequestBody UserRequest request) {

        if (userService.existsByUsername(request.username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new BaseResponse<>(new MetaResponse(false, "Username is already taken"), null)
            );
        }

        if (request.username == null || request.username.trim().isEmpty() ||
                request.password == null || request.password.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse<>(new MetaResponse(false, "User not found"), null)
            );
        }

        User newUser = userService.registerUser(request.username, request.password);

        // Deklarasi dan inisialisasi responseData dengan HashMap
        Map<String, String> responseData = new HashMap<>();
        responseData.put("userId", newUser.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new BaseResponse<>(new MetaResponse(true, "User registered successfully"), responseData)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<Map<String, String>>> login(@RequestBody UserRequest request) {
        Optional<User> userOpt = userService.findByUsername(request.username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (BCrypt.checkpw(request.password, user.getPassword())) {
                // Menggunakan Map untuk menyimpan userId agar format JSON tetap seragam
                Map<String, String> responseData = new HashMap<>();
                responseData.put("userId", user.getUserId());

                return ResponseEntity.ok(new BaseResponse<>(
                        new MetaResponse(true, "Login successful"), responseData
                ));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new BaseResponse<>(new MetaResponse(false, "Wrong password"), null)
                );
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new BaseResponse<>(new MetaResponse(false, "User not found"), null)
        );
    }


    @GetMapping("/{userId}")
    public ResponseEntity<BaseResponse<User>> getUserById(@PathVariable String userId) {
        return userService.getUserById(userId)
                .map(user -> ResponseEntity.ok(new BaseResponse<>(
                        new MetaResponse(true, "User found"), user)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new BaseResponse<>(new MetaResponse(false, "User not found"), null)));
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponse<Map<String, String>>> updateUsername(@RequestBody UserUpdate request) {
        if (userService.existsByUsername(request.username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new BaseResponse<>(new MetaResponse(false, "Username is already taken"), null)
            );
        }
        userService.updateUsername(request.userId, request.username);

        return ResponseEntity.ok(new BaseResponse<>(
                new MetaResponse(true, "Username updated successfully"),null
        ));
    }
}
