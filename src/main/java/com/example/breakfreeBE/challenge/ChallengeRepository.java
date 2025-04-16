package com.example.breakfreeBE.challenge;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, String> {
    Optional<Challenge> findByChallengeIdAndUserId(String challengeId, String userId);
}
