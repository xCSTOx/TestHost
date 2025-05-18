package com.example.breakfreeBE.addiction.controller;

import com.example.breakfreeBE.achievement.entity.Achievement;
import com.example.breakfreeBE.achievement.repository.AchievementRepository;
import com.example.breakfreeBE.achievement.repository.AchievementUserRepository;
import com.example.breakfreeBE.addiction.dto.AddictionDTO;
import com.example.breakfreeBE.addiction.repository.AddictionRepository;
import com.example.breakfreeBE.addiction.service.AddictionService;
import com.example.breakfreeBE.common.BaseResponse;
import com.example.breakfreeBE.common.MetaResponse;
import com.example.breakfreeBE.userRegistration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/addictions")
public class AddictionController {


    private AddictionService addictionService;
    private AchievementUserRepository achievementUserRepository;
    private AchievementRepository achievementRepository;
    private AddictionRepository addictionRepository;
    private UserRepository userRepository;

    @Autowired
    public AddictionController(AddictionService addictionService, AchievementUserRepository achievementUserRepository, AchievementRepository achievementRepository, AddictionRepository addictionRepository, UserRepository userRepository) {
        this.addictionService = addictionService;
        this.achievementUserRepository = achievementUserRepository;
        this.achievementRepository = achievementRepository;
        this.addictionRepository = addictionRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/one")
    public ResponseEntity<BaseResponse<Map<String, Object>>> getOneAddiction(@RequestBody AddictionDTO requestDto) {
        if (requestDto.getUserId() == null || requestDto.getUserId().isBlank() ||
                requestDto.getAddictionId() == null || requestDto.getAddictionId().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new BaseResponse<>(new MetaResponse(false, "Addiction not found"), null)
            );
        }

        Optional<AddictionDTO> addiction = addictionService.getAddictionByUserIdAndAddictionId(
                requestDto.getUserId(), requestDto.getAddictionId());

        if (addiction.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse<>(new MetaResponse(false, "Addiction not found"), null)
            );
        }

        AddictionDTO foundAddiction = addiction.get();

        // Create a map with only the required fields
        Map<String, Object> oneResponse = new HashMap<>();
        oneResponse.put("saver", foundAddiction.getSaver());
        oneResponse.put("motivation", foundAddiction.getMotivation());
        oneResponse.put("startDate", foundAddiction.getStartDate());
        oneResponse.put("addictionName", foundAddiction.getAddictionName());

        return ResponseEntity.ok(
                new BaseResponse<>(new MetaResponse(true, "Addiction retrieved successfully"), oneResponse)
        );
    }

    @PostMapping("/view")
    public ResponseEntity<BaseResponse<List<AddictionDTO>>> getAddictionsByUser(@RequestBody AddictionDTO requestDto) {
        if (requestDto.getUserId() == null || requestDto.getUserId().isBlank()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse<>(new MetaResponse(false, "Addiction not found"), null)
            );
        }

        List<AddictionDTO> addictions = addictionService.getAddictionsByUser(requestDto.getUserId());

        for (AddictionDTO addiction : addictions) {
            if (addiction.getStartDate() != null) {
                // Konversi timestamp ke LocalDate
                LocalDate startDate = Instant.ofEpochMilli(addiction.getStartDate())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                long currentStreak = ChronoUnit.DAYS.between(startDate, LocalDate.now());
                addiction.setCurrentStreak(currentStreak);

                // Bandingkan dengan streak tersimpan (bisa null)
                Long savedStreak = addiction.getStreaks();
                long longestStreak = (savedStreak != null) ? Math.max(savedStreak, currentStreak) : currentStreak;
                addiction.setLongestStreak(longestStreak);

                AddictionService.StreakFunFact streakFact = addictionService.getStreakFunFact(currentStreak);
                addiction.setStreakFunFact(streakFact);
            }
        }

        return ResponseEntity.ok(
                new BaseResponse<>(new MetaResponse(true, "Addiction list retrieved successfully"), addictions)
        );
    }

    @PostMapping("/saver")
    public ResponseEntity<BaseResponse<Map<String, Object>>> getFunFactSaver(@RequestBody AddictionDTO requestDto) {
        if (requestDto.getUserId() == null || requestDto.getUserId().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new BaseResponse<>(new MetaResponse(false, "Addiction not found"), null)
            );
        }

        AddictionService.StreakFunFact savingFunFact = addictionService.getSavingFunFact(requestDto.getUserId());

        if (savingFunFact == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse<>(new MetaResponse(false, "Addiction not found"), null)
            );
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("funfact", savingFunFact.fact);
        responseData.put("image_url", savingFunFact.imageUrl);
        responseData.put("color", savingFunFact.bgColor);

        return ResponseEntity.ok(
                new BaseResponse<>(new MetaResponse(true, "Successfully create funfact saving"), responseData)
        );
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse<Map<String, Object>>> createAddiction(@RequestBody AddictionDTO dto) {
        if (dto.getUserId() == null || dto.getUserId().isBlank() ||
                dto.getAddictionId() == null || dto.getAddictionId().isBlank() ||
                dto.getStartDate() == null ||
                dto.getMotivation() == null || dto.getMotivation().isBlank() ||
                dto.getSaver() == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new BaseResponse<>(new MetaResponse(false, "Addiction not found"), null)
            );
        }

        Optional<AddictionDTO> newAddiction = addictionService.saveAddiction(dto);

        if (newAddiction.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new BaseResponse<>(new MetaResponse(false, "Addiction already exists for this user"), null)
            );
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("addictionId", newAddiction.get().getAddictionId());

        // Cek apakah ini addiction pertama untuk user
        long addictionCount = addictionRepository.countByUserId(dto.getUserId());
        boolean isFirstAddiction = addictionCount == 1;

        if (isFirstAddiction) {
            String achievementId = "AC0003";
            Optional<Achievement> achOpt = achievementRepository.findById(achievementId);
            if (achOpt.isPresent()) {
                Achievement ach = achOpt.get();

                Map<String, String> achievementMap = new HashMap<>();
                achievementMap.put("achievementId", ach.getAchievementId());
                achievementMap.put("achievementName", ach.getAchievementName());
                achievementMap.put("achievementUrl", ach.getAchievementUrl());

                responseData.put("achievement", achievementMap);
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new BaseResponse<>(new MetaResponse(true, "Addiction record created successfully"), responseData)
        );
    }


    @PutMapping("/update")
    public ResponseEntity<BaseResponse<Map<String, String>>> updateAddiction(@RequestBody AddictionDTO dto) {
        Optional<AddictionDTO> updatedAddiction = addictionService.updateAddiction(dto);

        if (updatedAddiction.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse<>(new MetaResponse(false, "Addiction not found"), null)
            );
        }

        return ResponseEntity.ok(new BaseResponse<>(new MetaResponse(true, "Addiction updated successfully"), null));
    }

    @PatchMapping("/reset")
    public ResponseEntity<BaseResponse<Map<String, String>>> resetAddiction(@RequestBody AddictionDTO dto) {
        Optional<AddictionDTO> resetAddiction = addictionService.resetAddiction(dto);

        if (resetAddiction.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse<>(new MetaResponse(false, "Addiction not found"), null)
            );
        }

        return ResponseEntity.ok(new BaseResponse<>(new MetaResponse(true, "Addiction reset successfully"), null));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse<Map<String, String>>> deleteAddiction(@RequestBody AddictionDTO dto) {
        boolean isDeleted = addictionService.deleteAddiction(dto);

        if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse<>(new MetaResponse(false, "Addiction not found"), null)
            );
        }

        return ResponseEntity.ok(new BaseResponse<>(new MetaResponse(true, "Addiction deleted successfully"), null));
    }
}