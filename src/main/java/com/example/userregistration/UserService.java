package com.example.userregistration;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        // Validasi username minimal 3 karakter
        if (user.getUsername().length() < 3) {
            throw new RuntimeException("Username must be at least 3 characters long");
        }

        // Cek apakah username sudah ada di database
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            // Validasi username minimal 3 karakter saat update
            if (updatedUser.getUsername().length() < 3) {
                throw new RuntimeException("Username must be at least 3 characters long");
            }

            // Cek apakah username baru sudah digunakan oleh user lain
            if (!user.getUsername().equals(updatedUser.getUsername()) && userRepository.existsByUsername(updatedUser.getUsername())) {
                throw new RuntimeException("Username already exists!");
            }

            user.setUsername(updatedUser.getUsername());
            user.setPassword(updatedUser.getPassword());
            user.setAddiction(updatedUser.getAddiction());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
