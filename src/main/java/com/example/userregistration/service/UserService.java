package com.example.userregistration.service;

import com.example.userregistration.entity.User;
import com.example.userregistration.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public User registerUser(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists!");
        }

        String hashedPassword = passwordEncoder.encode(password);
        String newUserId = generateNextUserId();

        User newUser = new User(newUserId, username, hashedPassword);
        return userRepository.save(newUser);
    }

    private String generateNextUserId() {
        Optional<String> lastUserIdOpt = userRepository.findLastUserId();

        if (lastUserIdOpt.isPresent()) {
            String lastUserId = lastUserIdOpt.get();
            int nextId = Integer.parseInt(lastUserId.substring(2)) + 1;
            return String.format("US%04d", nextId);
        } else {
            return "US0001";
        }
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    public String updateUsername(String userId, String newUsername) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setUsername(newUsername);
            userRepository.save(user);
            return "Username successfully updated";
        }
        return "User not found";
    }
}
