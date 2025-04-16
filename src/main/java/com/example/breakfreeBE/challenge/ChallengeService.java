package com.example.breakfreeBE.challenge;

import com.example.breakfreeBE.challenge.dto.ChallengeDetailResponse;
import com.example.breakfreeBE.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeDataRepository challengeDataRepository;

    public ChallengeDetailResponse getChallengeDetail(String challengeId, String userId) {
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
                challenge.getStatus()
        );
    }
}
