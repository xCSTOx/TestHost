package com.example.breakfreeBE.userRegistration;

import com.example.breakfreeBE.userRegistration.common.BaseResponse;
import com.example.breakfreeBE.userRegistration.common.MetaResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(new BaseResponse<>(
                new MetaResponse(true, "Users retrieved successfully"),
                users
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<User>> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(new BaseResponse<>(
                        new MetaResponse(true, "User retrieved successfully"),
                        user
                )))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new BaseResponse<>(new MetaResponse(false, "User not found"), null)
                ));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<User>> createUser(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().length() < 3) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    new MetaResponse(false, "Username must be at least 3 characters long"), null
            ));
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    new MetaResponse(false, "Password cannot be empty"), null
            ));
        }

        User createdUser = userService.saveUser(user);
        createdUser.setPassword(null); // Jangan tampilkan password
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(
                new MetaResponse(true, "User registered successfully"),
                createdUser
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<User>> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            updatedUser.setPassword(null); // Jangan tampilkan password
            return ResponseEntity.ok(new BaseResponse<>(
                    new MetaResponse(true, "User updated successfully"),
                    updatedUser
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse<>(new MetaResponse(false, e.getMessage()), null)
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new BaseResponse<>(
                    new MetaResponse(true, "User deleted successfully"),
                    null
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse<>(new MetaResponse(false, e.getMessage()), null)
            );
        }
    }
}
