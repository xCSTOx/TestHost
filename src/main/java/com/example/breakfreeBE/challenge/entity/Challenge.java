package com.example.breakfreeBE.challenge.entity;

import com.example.breakfreeBE.userRegistration.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "challenges")
@Data
public class Challenge {

    @Id
    @Column(name = "challenge_id", length = 6)
    private String challengeId;

    @Column(name = "user_id", length = 6)
    private String userId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "challenge_id", insertable = false, updatable = false)
    private ChallengeData challengeData;

    private LocalDate startDate;
    private int timesComplete;
    private String status;
}
