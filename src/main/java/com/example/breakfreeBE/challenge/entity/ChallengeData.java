package com.example.breakfreeBE.challenge.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "challenges_data")
@Data
public class ChallengeData {

    @Id
    private String challengeId;

    private String addictionId;
    private String challengeName;
    private String challengeDesc;
    private int totalDays;
    private String color;
    private String challengeUrl;
}
