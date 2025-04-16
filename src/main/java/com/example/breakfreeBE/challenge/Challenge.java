package com.example.breakfreeBE.challenge;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "challenges")
@Data
public class Challenge {

    @Id
    private String challengeId;

    private String userId;
    private LocalDate startDate;
    private int timesComplete;
    private String status;
}
