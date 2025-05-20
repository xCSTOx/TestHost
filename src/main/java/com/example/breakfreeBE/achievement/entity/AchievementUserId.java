package com.example.breakfreeBE.achievement.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchievementUserId implements Serializable {
    private String achievementId;
    private String userId;
}
