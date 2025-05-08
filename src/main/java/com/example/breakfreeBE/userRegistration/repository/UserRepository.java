package com.example.breakfreeBE.userRegistration.repository;

import com.example.breakfreeBE.userRegistration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query("SELECT u.userId FROM User u ORDER BY u.userId DESC LIMIT 1")
    Optional<String> findLastUserId();
}


