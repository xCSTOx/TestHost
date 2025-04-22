package com.example.community.controller;

import com.example.community.dto.CommentDTO;
import com.example.community.entity.Comment;
import com.example.community.service.CommentService;
import com.example.common.BaseResponse;
import com.example.common.MetaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // Create a new comment
    @PostMapping("/create")
    public ResponseEntity<BaseResponse<Map<String, String>>> createComment(@RequestBody CommentDTO commentDTO) {
        try {
            // Validasi field-field yang diperlukan
            if (commentDTO.getPostId() == null || commentDTO.getUserId() == null ||
                    commentDTO.getCommentText() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new BaseResponse<>(new MetaResponse(false, "Comment not found"), null)
                );
            }

            CommentDTO createdComment = commentService.createComment(commentDTO);

            // Mengembalikan commentId dalam respons
            Map<String, String> responseData = new HashMap<>();
            responseData.put("commentId", createdComment.getCommentId());

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new BaseResponse<>(new MetaResponse(true, "Comment created successfully"), responseData)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new BaseResponse<>(new MetaResponse(false, "Failed to create comment: " + e.getMessage()), null)
            );
        }
    }

    // Get comments by post
    @PostMapping("/view")
    public ResponseEntity<BaseResponse<List<CommentDTO>>> getCommentsByPost(@RequestBody CommentDTO commentDTO) {
        try {
            // Validasi field yang diperlukan
            if (commentDTO.getPostId() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new BaseResponse<>(new MetaResponse(false, "Comment not found"), null)
                );
            }

            List<CommentDTO> comments = commentService.getCommentsByPost(commentDTO.getPostId());

            // Memastikan setiap CommentDTO hanya berisi username dan commentText
            // Ini tergantung implementasi DTO dan service Anda
            // Jika perlu filter data di controller, bisa ditambahkan di sini

            return ResponseEntity.ok(
                    new BaseResponse<>(new MetaResponse(true, "Comments retrieved successfully"), comments)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new BaseResponse<>(new MetaResponse(false, "Failed to retrieve comments: " + e.getMessage()), null)
            );
        }
    }
}