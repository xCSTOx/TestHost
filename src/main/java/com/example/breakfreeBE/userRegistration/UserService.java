package com.example.breakfreeBE.userRegistration;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@Service
class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        if (user.getUsername() == null || user.getUsername().length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must be at least 3 characters long");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }

        if (!isValidPassword(user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters long, contain at least one uppercase letter, and one number.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            if (updatedUser.getUsername() == null || updatedUser.getUsername().length() < 3) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must be at least 3 characters long");
            }

            if (!user.getUsername().equals(updatedUser.getUsername()) &&
                    userRepository.existsByUsername(updatedUser.getUsername())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists!");
            }

            user.setUsername(updatedUser.getUsername());

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
                if (!isValidPassword(updatedUser.getPassword())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters long, contain at least one uppercase letter, and one number.");
                }
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            return userRepository.save(user);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + id + " not found"));
    }

    private boolean isValidPassword(String password) {
        return password != null &&
                password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&  // Minimal satu huruf besar
                password.matches(".*\\d.*");     // Minimal satu angka
    }
}
