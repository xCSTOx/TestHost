package com.example.breakfreeBE.achievement;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "achievement_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchievementUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "achievement_id")
    private String achievementId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "achievement_date")
    private Date achievementDate;
}
