package com.example.breakfreeBE.challenge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "challenge_progress")
public class ChallengeProgress {

    @Id
    @Column(name = "progress_id")
    private String progressId;

    @Column(name = "progress_date", nullable = false)
    private LocalDate progressDate;

    @Column(name = "challenge_id", nullable = false)
    private String challengeId;

    @Column(name = "user_id", nullable = false)
    private String userId;

}

