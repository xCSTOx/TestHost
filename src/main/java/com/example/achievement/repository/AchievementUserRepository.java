package com.example.achievement.repository;

import com.example.achievement.entity.AchievementUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementUserRepository extends JpaRepository<AchievementUser, String> {
    List<AchievementUser> findByUserId(String userId);
}
