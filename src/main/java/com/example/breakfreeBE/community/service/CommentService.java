package com.example.breakfreeBE.community.service;

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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;


    // Create a new comment
    @Transactional
    public CommentDTO createComment(CommentDTO commentDTO) {
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
        comment.setCommentDate(LocalDate.now()); // Current date

        // Save comment
        Comment savedComment = commentRepository.save(comment);

        return convertToDTO(savedComment);
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

        // Fetch username from User repository
        Optional<User> user = userRepository.findById(comment.getUserId());
        user.ifPresent(u -> dto.setUsername(u.getUsername()));

        return dto;
    }
}