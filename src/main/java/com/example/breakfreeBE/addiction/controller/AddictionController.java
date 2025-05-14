package com.example.breakfreeBE.addiction.controller;

import com.example.breakfreeBE.addiction.dto.AddictionDTO;
import com.example.breakfreeBE.addiction.service.AddictionService;
import com.example.breakfreeBE.common.BaseResponse;
import com.example.breakfreeBE.common.MetaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/addictions")
public class AddictionController {

    @Autowired
    private AddictionService addictionService;

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
                long streakDays = ChronoUnit.DAYS.between(addiction.getStartDate(), LocalDate.now());
                AddictionService.StreakFunFact streakFact = addictionService.getStreakFunFact(streakDays);
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
    public ResponseEntity<BaseResponse<Map<String, String>>> createAddiction(@RequestBody AddictionDTO dto) {
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

        Map<String, String> responseData = new HashMap<>();
        responseData.put("addictionId", newAddiction.get().getAddictionId());

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
