package com.example.community.repository;

import com.example.community.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    // Find comments by user ID
    List<Comment> findByUserId(String userId);

    // Find comments by post ID
    List<Comment> findByPostId(String postId);

    // Delete comments by post ID
    void deleteByPostId(String postId);

    List<Comment> findByPostIdOrderByCommentIdDesc(String postId);

    @Query("SELECT c.commentId FROM Comment c ORDER BY c.commentId DESC LIMIT 1")
    Optional<String> findLastCommentId();
}

