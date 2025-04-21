package com.example.breakfreeBE.achievement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementUserRepository extends JpaRepository<AchievementUser, String> {
    List<AchievementUser> findByUserId(String userId);
}
