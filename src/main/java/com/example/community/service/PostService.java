package com.example.community.service;

import com.example.community.dto.CommentDTO;
import com.example.community.entity.Comment;
import com.example.community.entity.Post;
import com.example.userregistration.entity.User;
import com.example.community.repository.CommentRepository;
import com.example.community.repository.PostRepository;
import com.example.userregistration.repository.UserRepository;
import com.example.community.dto.PostDTO;
import com.example.community.dto.PostDetailDTO;
import com.example.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentService commentService;

    public PostDTO createPost(PostDTO postDTO) {
        // Generate post ID
        String postId = generatePostId();

        // Create new post
        Post post = new Post();
        post.setPostId(postId);
        post.setUserId(postDTO.getUserId());
        post.setPostText(postDTO.getPostText());
        post.setPostDate(LocalDate.now()); // Current date
        if (postDTO.getAchievementId() != null) {
            post.setAchievementId(postDTO.getAchievementId());
        }

        if (postDTO.getAddictionId() != null) {
            post.setAddictionId(postDTO.getAddictionId());
        }

        if (postDTO.getChallengeId() != null) {
            post.setChallengeId(postDTO.getChallengeId());
        }
        // Save post
        Post savedPost = postRepository.save(post);

        return convertToDTOP(savedPost);
    }

    @Transactional
    public void deletePost(String postId, String userId) {
        // Check if post exists
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();

            // Check if the user is authorized to delete this post
            if (post.getUserId().equals(userId)) {
                // Delete all comments related to this post
                commentRepository.deleteByPostId(postId);

                // Delete the post
                postRepository.deleteById(postId);
            } else {
                throw new RuntimeException("User is not authorized to delete this post");
            }
        } else {
            throw new RuntimeException("Post not found with id: " + postId);
        }
    }

    public PostDTO updatePost(PostDTO postDTO) {
        // Check if post exists
        Optional<Post> postOptional = postRepository.findById(postDTO.getPostId());

        if (postOptional.isPresent()) {
            Post post = postOptional.get();

            // Check if the user is authorized to update this post
            if (post.getUserId().equals(postDTO.getUserId())) {
                // Update post fields
                post.setPostText(postDTO.getPostText());

                // Update optional fields if present in the request
                if (postDTO.getAchievementId() != null) {
                    post.setAchievementId(postDTO.getAchievementId());
                }

                if (postDTO.getAddictionId() != null) {
                    post.setAddictionId(postDTO.getAddictionId());
                }

                if (postDTO.getChallengeId() != null) {
                    post.setChallengeId(postDTO.getChallengeId());
                }

                // Save updated post
                Post updatedPost = postRepository.save(post);

                return convertToDTOP(updatedPost);
            } else {
                throw new RuntimeException("User is not authorized to update this post");
            }
        } else {
            throw new RuntimeException("Post not found with id: " + postDTO.getPostId());
        }
    }

    public PostDetailDTO getPostDetailsById(String postId, String userId) {
        // Check if post exists
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();

            // Get comments for this post
            List<Comment> comments = commentRepository.findByPostIdOrderByCommentIdDesc(postId);

            // Map comments to CommentDTOs with username
            List<CommentDTO> commentsu = comments.stream()
                    .map(comment -> {
                        CommentDTO dto = commentService.convertToDTO(comment);
                        Optional<User> commentUser = userRepository.findById(comment.getUserId());
                        commentUser.ifPresent(user -> dto.setUsername(user.getUsername()));
                        return dto;
                    })
                    .collect(Collectors.toList());

            // Create PostDetailDTO
            PostDetailDTO postDetailDTO = new PostDetailDTO();
            postDetailDTO.setPost(convertToDTOP(post));

            // Gunakan commentDTOs, bukan comments
            postDetailDTO.setCommentsu(commentsu); // Ubah ini di PostDetailDTO

            return postDetailDTO;
        } else {
            throw new RuntimeException("Post not found with id: " + postId);
        }
    }

    // Method to get all posts sorted by postId in descending order (newest first)
    public List<PostDTO> getAllPosts() {
        // Using Spring Data's Sort capability to sort by postId in descending order
        List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "postId"));

        return posts.stream()
                .map(this::convertToDTOP)
                .collect(Collectors.toList());
    }

    public List<PostDTO> getPostsByUserId(String userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return posts.stream()
                .map(this::convertToDTOP)
                .collect(Collectors.toList());
    }

    // Generate post ID with the format P00001, P00002, etc.
    private String generatePostId() {
        Optional<String> lastPostIdOpt = postRepository.findLastPostId();
        String lastPostId = lastPostIdOpt.orElse("P00000");

        try {
            int numericPart = Integer.parseInt(lastPostId.substring(1));
            return String.format("P%05d", numericPart + 1);
        } catch (NumberFormatException e) {
            // log error dan fallback
            System.err.println("Format lastPostId tidak valid: " + lastPostId);
            return "P00001";
        }
    }


    // Helper method to convert Post to PostDTO
    public PostDTO convertToDTOP(Post post) {
        PostDTO dto = new PostDTO();
        dto.setPostId(post.getPostId());
        dto.setUserId(post.getUserId());
        dto.setPostText(post.getPostText());
        dto.setPostDate(post.getPostDate());
        dto.setAchievementId(post.getAchievementId());
        dto.setAddictionId(post.getAddictionId());
        dto.setChallengeId(post.getChallengeId());
        // Fetch username from User repository
        Optional<User> user = userRepository.findById(post.getUserId());
        user.ifPresent(u -> dto.setUsername(u.getUsername()));
        return dto;
    }


}