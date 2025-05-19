package com.example.breakfreeBE.community.service;

import com.example.breakfreeBE.achievement.entity.Achievement;
import com.example.breakfreeBE.achievement.entity.AchievementUser;
import com.example.breakfreeBE.achievement.entity.AchievementUserId;
import com.example.breakfreeBE.achievement.repository.AchievementRepository;
import com.example.breakfreeBE.achievement.repository.AchievementUserRepository;
import com.example.breakfreeBE.avatar.entity.Avatar;
import com.example.breakfreeBE.avatar.repository.AvatarRepository;
import com.example.breakfreeBE.community.dto.CommentDTO;
import com.example.breakfreeBE.community.entity.Comment;
import com.example.breakfreeBE.community.entity.Post;
import com.example.breakfreeBE.community.repository.CommentRepository;
import com.example.breakfreeBE.community.repository.PostRepository;
import com.example.breakfreeBE.userRegistration.entity.User;
import com.example.breakfreeBE.userRegistration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    // Achievement ID constant for first comment
    private static final String FIRST_COMMENT_ACHIEVEMENT_ID = "AC0007";

    private UserRepository userRepository;
    private AchievementUserRepository achievementUserRepository;
    private AchievementRepository achievementRepository;
    private CommentRepository commentRepository;
    private AvatarRepository avatarRepository;
    private PostRepository postRepository;

    @Autowired
    public CommentService(AchievementRepository achievementRepository, AchievementUserRepository achievementUserRepository, UserRepository userRepository, CommentRepository commentRepository, AvatarRepository avatarRepository, PostRepository postRepository
    ){
        this.achievementRepository = achievementRepository;
        this.achievementUserRepository = achievementUserRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.avatarRepository = avatarRepository;
        this.postRepository = postRepository;
    }

    // Create a new comment
    @Transactional
    public Map<String, Object> createComment(CommentDTO commentDTO) {
        // Check if post exists
        Optional<Post> postOptional = postRepository.findById(commentDTO.getPostId());

        // Generate comment ID with format C00001, C00002, etc.
        String commentId = generateCommentId();

        // Create new comment
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setPostId(commentDTO.getPostId());
        comment.setUserId(commentDTO.getUserId());
        comment.setCommentText(commentDTO.getCommentText());
        comment.setCommentDate(Instant.now().toEpochMilli());

        // Save comment
        Comment savedComment = commentRepository.save(comment);

        Map<String, Object> result = new HashMap<>();
        result.put("comment", convertToDTO(savedComment));

        // Check for achievements
        commentAchievement(commentDTO.getUserId(), result);

        return result;
    }

    private void commentAchievement(String userId, Map<String, Object> result) {
        // Count comments by this user
        long commentCount = commentRepository.countByUserId(userId);

        // If this is their first comment (count should be 1)
        if (commentCount == 1) {
            // Check if user already has the achievement
            boolean hasAchievement = achievementUserRepository.existsById_UserIdAndId_AchievementId(userId, FIRST_COMMENT_ACHIEVEMENT_ID);

            if (!hasAchievement) {
                // Get achievement details
                Optional<Achievement> achievementOpt = achievementRepository.findById(FIRST_COMMENT_ACHIEVEMENT_ID);

                if (achievementOpt.isPresent()) {
                    Achievement achievement = achievementOpt.get();

                    // Get user
                    Optional<User> userOpt = userRepository.findById(userId);
                    if (userOpt.isPresent()) {
                        User user = userOpt.get();

                        // Create achievement user record
                        AchievementUserId achievementUserId = new AchievementUserId();
                        achievementUserId.setUserId(userId);
                        achievementUserId.setAchievementId(FIRST_COMMENT_ACHIEVEMENT_ID);

                        AchievementUser achievementUser = new AchievementUser();
                        achievementUser.setId(achievementUserId);
                        achievementUser.setUser(user);
                        achievementUser.setAchievement(achievement);
                        achievementUser.setAchievementDate(System.currentTimeMillis());

                        // Save achievement
                        achievementUserRepository.save(achievementUser);

                        // Return achievement data for response
                        Map<String, String> achievementInfo = new HashMap<>();
                        achievementInfo.put("achievementId", achievement.getAchievementId());
                        achievementInfo.put("achievementName", achievement.getAchievementName());
                        achievementInfo.put("achievementUrl", achievement.getAchievementUrl());

                        result.put("achievement", achievementInfo);
                    }
                }
            }
        }
    }

    // Get all comments by Post ID
    public List<CommentDTO> getCommentsByPost(String postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCommentIdDesc(postId);
        return comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    // Helper method to generate next comment ID with format C00001
    private String generateCommentId() {
        Optional<String> lastCommentIdOpt = commentRepository.findLastCommentId();

        if (lastCommentIdOpt.isPresent()) {
            String lastCommentId = lastCommentIdOpt.get();
            int nextId = Integer.parseInt(lastCommentId.substring(1)) + 1;
            return String.format("C%05d", nextId);
        } else {
            return "C00001";
        }
    }

    // Helper method to convert Comment to CommentDTO
    public CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setCommentId(comment.getCommentId());
        dto.setPostId(comment.getPostId());
        dto.setUserId(comment.getUserId());
        dto.setCommentText(comment.getCommentText());
        dto.setCommentDate(comment.getCommentDate());

        Optional<User> user = userRepository.findById(comment.getUserId());
        if (user.isPresent()) {
            User userEntity = user.get();
            dto.setUsername(userEntity.getUsername());

            // Fetch avatar URL if avatar ID exists
            String avatarId = userEntity.getAvatarId();
            if (avatarId != null) {
                Optional<Avatar> avatar = avatarRepository.findById(avatarId);
                avatar.ifPresent(a -> dto.setAvatarUrl(a.getAvatarUrl()));
            }
        }

        return dto;
    }
}