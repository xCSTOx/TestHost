package com.example.breakfreeBE.community.service;

import com.example.breakfreeBE.achievement.repository.AchievementRepository;
import com.example.breakfreeBE.addiction.repository.AddictionDataRepository;
import com.example.breakfreeBE.addiction.repository.AddictionRepository;
import com.example.breakfreeBE.avatar.repository.AvatarRepository;
import com.example.breakfreeBE.challenge.repository.ChallengeDataRepository;
import com.example.breakfreeBE.challenge.repository.ChallengeRepository;
import com.example.breakfreeBE.community.dto.CommentDTO;
import com.example.breakfreeBE.community.entity.Comment;
import com.example.breakfreeBE.community.entity.Post;
import com.example.breakfreeBE.community.repository.BookmarkedPostRepository;
import com.example.breakfreeBE.userRegistration.entity.User;
import com.example.breakfreeBE.community.repository.CommentRepository;
import com.example.breakfreeBE.community.repository.PostRepository;
import com.example.breakfreeBE.userRegistration.repository.UserRepository;
import com.example.breakfreeBE.community.dto.PostDTO;
import com.example.breakfreeBE.community.dto.PostRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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

    @Autowired
    private AvatarRepository avatarRepository;

    @Autowired
    private AddictionRepository addictionRepository;

    @Autowired
    private AddictionDataRepository addictionDataRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeDataRepository challengeDataRepository;

    @Autowired
    private BookmarkedPostRepository bookmarkedPostRepository;

    public PostDTO createPost(PostRequestDTO postRequestDTO) {
        String postId = generatePostId();

        Post post = new Post();
        post.setPostId(postId);
        post.setUserId(postRequestDTO.getUserId());
        post.setPostText(postRequestDTO.getPostText());
        post.setPostDate(postRequestDTO.getPostDate() != null ? postRequestDTO.getPostDate() : Instant.now().toEpochMilli());

        if (postRequestDTO.getAchievementId() != null) {
            post.setAchievementId(postRequestDTO.getAchievementId());
        }

        if (postRequestDTO.getAddictionId() != null) {
            post.setAddictionId(postRequestDTO.getAddictionId());
        }

        if (postRequestDTO.getChallengeId() != null) {
            post.setChallengeId(postRequestDTO.getChallengeId());
        }

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

    public PostDTO updatePost(PostRequestDTO postRequestDTO) {
        String postId = postRequestDTO.getPostId();
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();

            if (post.getUserId().equals(postRequestDTO.getUserId())) {
                post.setPostText(postRequestDTO.getPostText());

                if (postRequestDTO.getAchievementId() != null) {
                    post.setAchievementId(postRequestDTO.getAchievementId());
                }

                if (postRequestDTO.getAddictionId() != null) {
                    post.setAddictionId(postRequestDTO.getAddictionId());
                }

                if (postRequestDTO.getChallengeId() != null) {
                    post.setChallengeId(postRequestDTO.getChallengeId());
                }

                Post updatedPost = postRepository.save(post);
                return convertToDTOP(updatedPost);
            } else {
                throw new RuntimeException("User is not authorized to update this post");
            }
        } else {
            throw new RuntimeException("Post not found with id: " + postId);
        }
    }

    public PostDTO getPostDetailsById(String postId, String userId) {
        // Check if post exists
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            return convertToDTOP(post);
        } else {
            throw new RuntimeException("Post not found with id: " + postId);
        }
    }

    // (newest first)
    public List<PostDTO> getAllPosts() {
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
            // log error and fallback
            System.err.println("Invalid lastPostId format: " + lastPostId);
            return "P00001";
        }
    }

    // Helper method to convert Post to PostDTO
    public PostDTO convertToDTOP(Post post) {
        PostDTO dto = new PostDTO();
        dto.setPostId(post.getPostId());
        dto.setPostText(post.getPostText());
        dto.setPostDate(post.getPostDate());

        // --------- BOOKMARKED (Boolean) ---------
        boolean isBookmarked = bookmarkedPostRepository.existsByUserIdAndPostId(post.getUserId(), post.getPostId());
        dto.setBookmarked(isBookmarked);


        // --------- PROFILE DATA ---------
        userRepository.findById(post.getUserId()).ifPresent(user -> {
            PostDTO.ProfileDTO profileDTO = new PostDTO.ProfileDTO();
            profileDTO.setUserId(user.getUserId());
            profileDTO.setUsername(user.getUsername());

            if (user.getAvatarId() != null) {
                avatarRepository.findById(user.getAvatarId())
                        .ifPresent(avatar -> profileDTO.setAvatarUrl(avatar.getAvatarUrl()));
            }

            dto.setProfile(profileDTO);
        });

        // --------- STREAK DATA ---------
        if (post.getAddictionId() != null) {
            addictionRepository.findByUserIdAndAddictionId(post.getUserId(), post.getAddictionId())
                    .ifPresent(addiction -> {
                        PostDTO.StreakDataDTO streakData = new PostDTO.StreakDataDTO();
                        streakData.setAddictionId(addiction.getAddictionId());

                        // Nama addiksi dari addiction_data
                        addictionDataRepository.findById(addiction.getAddictionId())
                                .ifPresent(addictionData -> streakData.setAddictionName(addictionData.getAddictionName()));

                        long now = System.currentTimeMillis();
                        long startDate = addiction.getStartDate();
                        long calculatedStreak = (now - startDate) / (1000 * 60 * 60 * 24); // dalam hari
                        Long streaks = addiction.getStreaks();

                        if (streaks != null && streaks > calculatedStreak) {
                            streakData.setStreakDays(streaks);
                        } else {
                            streakData.setStreakDays(calculatedStreak);
                        }

                        dto.setStreakData(streakData);
                    });
        }

        // --------- ACHIEVEMENT DATA ---------
        if (post.getAchievementId() != null) {
            achievementRepository.findById(post.getAchievementId())
                    .ifPresent(achievement -> {
                        PostDTO.AchievementDataDTO achievementData = new PostDTO.AchievementDataDTO();
                        achievementData.setAchievementId(achievement.getAchievementId());
                        achievementData.setAchievementName(achievement.getAchievementName());
                        achievementData.setAchievementUrl(achievement.getAchievementUrl());
                        dto.setAchievementData(achievementData);
                    });
        }

        // --------- CHALLENGE DATA ---------
        if (post.getChallengeId() != null) {
            challengeRepository.findById(post.getChallengeId())
                    .ifPresent(challenge -> {
                        challengeDataRepository.findById(challenge.getChallengeDataId())
                                .ifPresent(challengeData -> {
                                    PostDTO.ChallengeDataDTO challengeDataDTO = new PostDTO.ChallengeDataDTO();
                                    challengeDataDTO.setChallengeId(challenge.getChallengeId());
                                    challengeDataDTO.setChallengeName(challengeData.getChallengeName());
                                    challengeDataDTO.setChallengeUrl(challengeData.getChallengeUrl());
                                    challengeDataDTO.setColor(challengeData.getColor());
                                    dto.setChallengeData(challengeDataDTO);
                                });
                    });
        }

        return dto;
    }
}