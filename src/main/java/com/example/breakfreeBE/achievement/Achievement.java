package com.example.breakfreeBE.achievement;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "achievement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Achievement {
    @Id
    @Column(name = "achievement_id")
    private String achievementId;

    @Column(name = "achievement_name")
    private String achievementName;

    @Column(name = "achievement_url")
    private String achievementUrl;
}
