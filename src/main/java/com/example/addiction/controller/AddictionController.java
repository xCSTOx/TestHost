package com.example.addiction.controller;

import com.example.addiction.common.BaseResponse;
import com.example.addiction.common.MetaResponse;
import com.example.addiction.entity.Addiction;
import com.example.addiction.service.AddictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/addictions")
public class AddictionController {

    @Autowired
    private AddictionService addictionService;

    @GetMapping("/{userId}")
    public ResponseEntity<BaseResponse<List<Addiction>>> getAddictionsByUser(@PathVariable String userId) {
        List<Addiction> addictions = addictionService.getAddictionsByUser(userId);
        return ResponseEntity.ok(new BaseResponse<>(new MetaResponse(true, "Addiction list retrieved successfully"), addictions));
    }

    @PostMapping("/")
    public ResponseEntity<BaseResponse<Map<String, String>>> createAddiction(@RequestBody Addiction addiction) {
        Optional<Addiction> newAddiction = addictionService.saveAddiction(addiction);

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
    public ResponseEntity<BaseResponse<Map<String, String>>> updateAddiction(@RequestBody Addiction addiction) {
        Optional<Addiction> updatedAddiction = addictionService.updateAddiction(addiction);

        if (updatedAddiction.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse<>(new MetaResponse(false, "Addiction not found"), null)
            );
        }

        return ResponseEntity.ok(new BaseResponse<>(new MetaResponse(true, "Addiction updated successfully"), null));
    }

    @PatchMapping("/reset")
    public ResponseEntity<BaseResponse<Map<String, String>>> resetAddiction(@RequestBody Addiction addiction) {
        Optional<Addiction> resetAddiction = addictionService.resetAddiction(addiction);

        if (resetAddiction.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse<>(new MetaResponse(false, "Addiction not found"), null)
            );
        }

        return ResponseEntity.ok(new BaseResponse<>(new MetaResponse(true, "Addiction reset successfully"), null));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse<Map<String, String>>> deleteAddiction(@RequestBody Addiction addiction) {
        boolean isDeleted = addictionService.deleteAddiction(addiction);

        if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse<>(new MetaResponse(false, "Addiction not found"), null)
            );
        }

        return ResponseEntity.ok(new BaseResponse<>(new MetaResponse(true, "Addiction deleted successfully"), null));
    }
}
