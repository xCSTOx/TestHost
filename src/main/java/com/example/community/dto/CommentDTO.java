package com.example.community.dto;

import lombok.Data;

import java.time.LocalDate;


@Data
public class CommentDTO {
    private String commentId;
    private String postId;
    private String userId;
    private String commentText;
    private LocalDate commentDate;
    private String username; // Join with User table to get username
}
