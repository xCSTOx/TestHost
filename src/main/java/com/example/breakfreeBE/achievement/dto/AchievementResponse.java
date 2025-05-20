package com.example.breakfreeBE.achievement.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AchievementResponse {
    private String achievementId;
    private String achievementName;
    private String achievementUrl;
}
