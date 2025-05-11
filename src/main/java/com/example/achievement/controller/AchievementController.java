package com.example.achievement.controller;

import com.example.achievement.dto.AchievementResponse;
import com.example.achievement.service.AchievementService;
import com.example.common.BaseResponse;
import com.example.common.MetaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/achievements")
@RequiredArgsConstructor
public class AchievementController {
    private final AchievementService achievementService;

    @GetMapping
    public BaseResponse<List<AchievementResponse>> getAllAchievements() {
        List<AchievementResponse> data = achievementService.getAllAchievements();
        return new BaseResponse<>(new MetaResponse(true, "Success"), data);
    }

    @GetMapping("/users/{userId}")
    public BaseResponse<List<AchievementResponse>> getUserAchievements(@PathVariable String userId) {
        List<AchievementResponse> data = achievementService.getAchievementsByUserId(userId);
        return new BaseResponse<>(new MetaResponse(true, "Success"), data);
    }
}
