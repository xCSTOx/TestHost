package com.example.breakfreeBE.challenge.entity;

import com.example.breakfreeBE.userRegistration.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "challenge")
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

    @Column(name = "times_complete", nullable = false)
    private int timesComplete;

    @Column(name = "status", nullable = false)
    private String status;

}
