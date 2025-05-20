package com.example.community.repository;

import com.example.community.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    // Find all posts by user ID
    List<Post> findByUserId(String userId);

    // Find all posts by addiction ID
    List<Post> findByAddictionId(String addictionId);

    // Find all posts by challenge ID
    List<Post> findByChallengeId(String challengeId);

    // Find all posts by achievement ID
    List<Post> findByAchievementId(String achievementId);

    // Find bookmarked posts by user ID
    @Query("SELECT p FROM Post p JOIN BookmarkedPost bp ON p.postId = bp.postId WHERE bp.userId = :userId")
    List<Post> findBookmarkedPostsByUserId(@Param("userId") String userId);

    // Custom query to find posts with their comment count
    @Query("SELECT p, COUNT(c) FROM Post p LEFT JOIN Comment c ON p.postId = c.postId GROUP BY p")
    List<Object[]> findPostsWithCommentCount();

    // Find the last/highest post ID for generating the next ID
    @Query("SELECT p.postId FROM Post p ORDER BY p.postId DESC LIMIT 1")
    Optional<String> findLastPostId();
}
