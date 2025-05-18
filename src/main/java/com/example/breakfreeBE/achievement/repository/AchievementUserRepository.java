package com.example.breakfreeBE.achievement.repository;

import com.example.breakfreeBE.achievement.entity.AchievementUser;
import com.example.breakfreeBE.achievement.entity.AchievementUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementUserRepository extends JpaRepository<AchievementUser, AchievementUserId> {

    List<AchievementUser> findById_UserId(String userId);

    // Perbaikan untuk mengecek keberadaan berdasarkan userId dan achievementId dalam ID komposit
    boolean existsById_UserIdAndId_AchievementId(String userId, String achievementId);

    // Perbaikan untuk mencari berdasarkan achievementId dalam ID komposit
    List<AchievementUser> findById_AchievementId(String achievementId);
}