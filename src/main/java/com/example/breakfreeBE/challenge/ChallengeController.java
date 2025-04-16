package com.example.breakfreeBE.challenge;

import com.example.breakfreeBE.challenge.dto.ChallengeDetailResponse;
import com.example.breakfreeBE.common.BaseResponse;
import com.example.breakfreeBE.common.MetaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/challenges")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    @GetMapping("/{challengeId}/users/{userId}/detail")
    public ResponseEntity<BaseResponse<ChallengeDetailResponse>> getChallengeDetail(
            @PathVariable String challengeId,
            @PathVariable String userId) {

        ChallengeDetailResponse response = challengeService.getChallengeDetail(challengeId, userId);
        return ResponseEntity.ok(new BaseResponse<>(new MetaResponse(true, "Challenge detail retrieved"), response));
    }
}
