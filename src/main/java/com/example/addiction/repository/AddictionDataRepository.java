package com.example.addiction.repository;

import com.example.addiction.entity.AddictionData;
import com.example.userregistration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddictionDataRepository extends JpaRepository<AddictionData, String> {
    Optional<AddictionData> findByAddictionId(String AddictionId);
}
