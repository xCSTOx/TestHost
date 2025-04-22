package com.example.community.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;


@Data
public class PostDTO {
    private String postId;
    private String userId;
    private String postText;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate postDate;
    private String achievementId;
    private String addictionId;
    private String challengeId;
    private String username; // Join with User table to get username
}

