package com.example.community.controller;

import com.example.community.service.PostService;
import com.example.community.dto.PostDTO;
import com.example.community.dto.PostDetailDTO;
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
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/view")
    public ResponseEntity<BaseResponse<List<PostDTO>>> getAllPosts() {
        try {
            List<PostDTO> posts = postService.getAllPosts();
            return ResponseEntity.ok(
                    new BaseResponse<>(new MetaResponse(true, "Posts retrieved successfully"), posts)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new BaseResponse<>(new MetaResponse(false, "Failed to retrieve posts: " + e.getMessage()), null)
            );
        }
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<Map<String, String>>> createPost(@RequestBody PostDTO postDTO) {
        try {
            // Validasi field-field yang diperlukan
            if (postDTO.getUserId() == null || postDTO.getPostText() == null || postDTO.getUserId().isBlank() || postDTO.getPostText().isBlank()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new BaseResponse<>(new MetaResponse(false, "Post not found"), null)
                );
            }

            PostDTO createdPost = postService.createPost(postDTO);

            Map<String, String> responseData = new HashMap<>();
            responseData.put("postId", createdPost.getPostId());

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new BaseResponse<>(new MetaResponse(true, "Post created successfully"), responseData)
            );
        } catch (Exception e) {
            e.printStackTrace(); // atau pakai logger
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new BaseResponse<>(new MetaResponse(false, "Failed to create post: " + e.getMessage()), null)
            );
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse<Void>> deletePost(@RequestBody Map<String, String> requestBody) {
        try {
            String postId = requestBody.get("postId");
            String userId = requestBody.get("userId");

            if (postId == null || userId == null || postId.isBlank() || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new BaseResponse<>(new MetaResponse(false, "Post not found"), null)
                );
            }

            postService.deletePost(postId, userId);

            return ResponseEntity.ok(
                    new BaseResponse<>(new MetaResponse(true, "Post deleted successfully"), null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new BaseResponse<>(new MetaResponse(false, "Failed to delete post: " + e.getMessage()), null)
            );
        }
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponse<Void>> updatePost(@RequestBody PostDTO postDTO) {
        try {
            // Validasi field-field yang diperlukan
            if (postDTO.getPostId() == null || postDTO.getUserId() == null || postDTO.getPostText() == null || postDTO.getPostId().isBlank() || postDTO.getUserId().isBlank() || postDTO.getPostText().isBlank()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new BaseResponse<>(new MetaResponse(false, "Post not found"), null)
                );
            }

            PostDTO updatedPost = postService.updatePost(postDTO);
            return ResponseEntity.ok(
                    new BaseResponse<>(new MetaResponse(true, "Post updated successfully"), null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new BaseResponse<>(new MetaResponse(false, "Failed to update post: " + e.getMessage()), null)
            );
        }
    }

    @PostMapping("/viewdetail")
    public ResponseEntity<BaseResponse<PostDetailDTO>> viewPostDetail(@RequestBody Map<String, String> requestBody) {
        try {
            String postId = requestBody.get("postId");
            String userId = requestBody.get("userId");

            if (postId == null || userId == null || postId.isBlank() || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new BaseResponse<>(new MetaResponse(false, "Post not found"), null)
                );
            }

            PostDetailDTO postDetail = postService.getPostDetailsById(postId, userId);

            return ResponseEntity.ok(
                    new BaseResponse<>(new MetaResponse(true, "Post details retrieved successfully"), postDetail)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new BaseResponse<>(new MetaResponse(false, "Failed to retrieve post details: " + e.getMessage()), null)
            );
        }
    }

    @PostMapping("/viewbyuser")
    public ResponseEntity<BaseResponse<List<PostDTO>>> getPostsByUserId(@RequestBody Map<String, String> requestBody) {
        try {
            String userId = requestBody.get("userId");

            if (userId == null || userId.isBlank()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new BaseResponse<>(new MetaResponse(false, "Post not found"), null)
                );
            }

            List<PostDTO> posts = postService.getPostsByUserId(userId);

            return ResponseEntity.ok(
                    new BaseResponse<>(new MetaResponse(true, "User posts retrieved successfully"), posts)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new BaseResponse<>(new MetaResponse(false, "Failed to retrieve user posts: " + e.getMessage()), null)
            );
        }
    }
}