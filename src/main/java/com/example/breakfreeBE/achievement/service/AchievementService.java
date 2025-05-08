package com.example.breakfreeBE.achievement.service;

import com.example.breakfreeBE.achievement.repository.AchievementRepository;
import com.example.breakfreeBE.achievement.repository.AchievementUserRepository;
import com.example.breakfreeBE.achievement.dto.AchievementResponse;
import com.example.breakfreeBE.achievement.entity.AchievementUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final AchievementUserRepository achievementUserRepository;

    public List<AchievementResponse> getAllAchievements() {
        return achievementRepository.findAll().stream()
                .map(a -> new AchievementResponse(a.getAchievementId(), a.getAchievementName(), a.getAchievementUrl()))
                .collect(Collectors.toList());
    }

    public List<AchievementResponse> getAchievementsByUserId(String userId) {
        List<AchievementUser> userAchievements = achievementUserRepository.findByUserId(userId);
        List<String> achievementIds = userAchievements.stream()
                .map(AchievementUser::getAchievementId)
                .collect(Collectors.toList());

        return achievementRepository.findAllById(achievementIds).stream()
                .map(a -> new AchievementResponse(a.getAchievementId(), a.getAchievementName(), a.getAchievementUrl()))
                .collect(Collectors.toList());
    }
}
