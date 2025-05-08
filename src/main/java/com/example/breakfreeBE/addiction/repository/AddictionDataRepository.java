package com.example.breakfreeBE.addiction.repository;

import com.example.breakfreeBE.addiction.entity.AddictionData;
import com.example.breakfreeBE.userRegistration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddictionDataRepository extends JpaRepository<AddictionData, String> {
    Optional<AddictionData> findByAddictionId(String AddictionId);
}