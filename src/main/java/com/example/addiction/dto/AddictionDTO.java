package com.example.addiction.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AddictionDTO {
    private String userId;
    private String addictionId;
    private Integer saver;
    private Long streaks;
    private String motivation;
    private LocalDate startDate;

    private String addictionName; // dari relasi AddictionData
}
