package com.example.breakfreeBE.challenge.service;

import com.example.breakfreeBE.challenge.dto.ChallengeDetailResponse;
import com.example.breakfreeBE.challenge.dto.ChallengeUserRequest;
import com.example.breakfreeBE.challenge.entity.Challenge;
import com.example.breakfreeBE.challenge.entity.ChallengeData;
import com.example.breakfreeBE.challenge.entity.ChallengeProgress;
import com.example.breakfreeBE.challenge.repository.ChallengeDataRepository;
import com.example.breakfreeBE.challenge.repository.ChallengeProgressRepository;
import com.example.breakfreeBE.challenge.repository.ChallengeRepository;
import com.example.breakfreeBE.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeDataRepository challengeDataRepository;

    @Autowired
    private ChallengeProgressRepository challengeProgressRepository;

    public List<ChallengeData> getAllDailyChallenges() {
        return challengeDataRepository.findAll();
    }

    public List<Challenge> getOngoingChallenges(String userId) {
        return challengeRepository.findByUserIdAndStatus(userId, "ongoing");
    }

    public void participateChallenge(ChallengeUserRequest request) {
        String challengeId = request.getChallengeId();
        String userId = request.getUserId();

        challengeDataRepository.findById(challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge not found"));

        boolean alreadyJoined = challengeRepository.findByChallengeIdAndUserId(challengeId, userId).isPresent();
        if (alreadyJoined) {
            throw new IllegalStateException("User already joined this challenge");
        }

        Challenge challenge = new Challenge();
        challenge.setChallengeId(challengeId);
        challenge.setUserId(userId);
        challenge.setStatus("ongoing");
        challenge.setStartDate(LocalDate.now());
        challenge.setTimesComplete(0);
        challengeRepository.save(challenge);
    }


    @Transactional
    public void stopChallenge(ChallengeUserRequest request) {
        challengeRepository.deleteByChallengeIdAndUserId(request.getChallengeId(), request.getUserId());
    }

    public void updateProgress(ChallengeUserRequest request) {
        String challengeId = request.getChallengeId();
        String userId = request.getUserId();

        Challenge challenge = challengeRepository.findByChallengeIdAndUserId(challengeId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge not found"));

        ChallengeProgress progress = new ChallengeProgress();
        progress.setProgressId(UUID.randomUUID().toString());
        progress.setChallengeId(challengeId);
        progress.setUserId(userId);
        progress.setProgressDate(LocalDate.now());
        challengeProgressRepository.save(progress);

        int count = challengeProgressRepository.countByChallengeIdAndUserId(challengeId, userId);
        challenge.setTimesComplete(count);

        ChallengeData data = challengeDataRepository.findById(challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge data not found"));

        if (count >= data.getTotalDays()) {
            challenge.setStatus("completed");
        }

        challengeRepository.save(challenge);
    }

    public List<Challenge> getCompletedChallenges(String userId) {
        return challengeRepository.findByUserIdAndStatus(userId, "completed");
    }

    public ChallengeDetailResponse getChallengeDetail(ChallengeUserRequest request) {
        String challengeId = request.getChallengeId();
        String userId = request.getUserId();

        Challenge challenge = challengeRepository.findByChallengeIdAndUserId(challengeId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge not found"));

        ChallengeData data = challengeDataRepository.findById(challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge data not found"));

        return new ChallengeDetailResponse(
                challenge.getChallengeId(),
                challenge.getUserId(),
                data.getChallengeName(),
                data.getChallengeDesc(),
                data.getTotalDays(),
                data.getColor(),
                data.getChallengeUrl(),
                challenge.getStartDate(),
                challenge.getTimesComplete(),
                challenge.getStatus(),
                data.getAddictionData().getAddictionName()
        );
    }
}
