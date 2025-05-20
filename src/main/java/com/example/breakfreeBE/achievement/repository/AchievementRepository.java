package com.example.breakfreeBE.achievement.repository;

import com.example.breakfreeBE.achievement.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, String> {
}
