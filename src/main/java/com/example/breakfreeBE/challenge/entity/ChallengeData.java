package com.example.breakfreeBE.challenge.entity;

import com.example.breakfreeBE.addiction.entity.AddictionData;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "challenge_data")
@Data
public class ChallengeData {

    @Id
    private String challengeId;

    @Column(name = "addiction_id", insertable = false, updatable = false)
    private String addictionId;

    @Column(name = "challenge_name")
    private String challengeName;

    @Column(name = "challenge_desc")
    private String challengeDesc;

    @Column(name = "total_days")
    private int totalDays;

    @Column(name = "color")
    private String color;

    @Column(name = "challenge_url")
    private String challengeUrl;

    @ManyToOne
    @JoinColumn(name = "addiction_id")
    private AddictionData addictionData;
}
