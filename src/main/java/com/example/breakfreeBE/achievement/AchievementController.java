package com.example.breakfreeBE.achievement;

import com.example.breakfreeBE.achievement.dto.AchievementResponse;
import com.example.breakfreeBE.common.BaseResponse;
import com.example.breakfreeBE.common.MetaResponse;
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
