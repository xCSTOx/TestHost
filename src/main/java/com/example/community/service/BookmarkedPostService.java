package com.example.community.service;

import com.example.community.dto.BookmarkedRequestDTO;
import com.example.community.entity.BookmarkedPost;
import com.example.community.entity.Post;
import com.example.community.repository.BookmarkedPostRepository;
import com.example.community.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookmarkedPostService {

    @Autowired
    private BookmarkedPostRepository bookmarkedPostRepository;

    @Autowired
    private PostRepository postRepository;

    /**
     * Bookmark a post
     * @param bookmarkRequest containing userId and postId
     * @return true if bookmark was successful, false if already bookmarked
     */
    public boolean bookmarkPost(BookmarkedRequestDTO bookmarkRequest) {
        // Check if already bookmarked
        if (bookmarkedPostRepository.existsByUserIdAndPostId(
                bookmarkRequest.getUserId(), bookmarkRequest.getPostId())) {
            return false;
        }

        // Create new bookmark
        BookmarkedPost bookmark = new BookmarkedPost();
        bookmark.setUserId(bookmarkRequest.getUserId());
        bookmark.setPostId(bookmarkRequest.getPostId());

        // Save bookmark
        bookmarkedPostRepository.save(bookmark);
        return true;
    }

    /**
     * Remove a bookmark
     * @param bookmarkRequest containing userId and postId
     * @return true if unbookmark was successful, false if not found
     */
    @Transactional
    public boolean unbookmarkPost(BookmarkedRequestDTO bookmarkRequest) {
        if (!bookmarkedPostRepository.existsByUserIdAndPostId(
                bookmarkRequest.getUserId(), bookmarkRequest.getPostId())) {
            return false;
        }

        bookmarkedPostRepository.deleteByUserIdAndPostId(
                bookmarkRequest.getUserId(), bookmarkRequest.getPostId());
        return true;
    }

    /**
     * View all bookmarked posts for a user
     * @param userId the user ID
     * @return List of posts that were bookmarked
     */
    public List<Post> viewBookmarkedPosts(String userId) {
        List<BookmarkedPost> bookmarks = bookmarkedPostRepository.findByUserId(userId);
        List<Post> posts = new ArrayList<>();

        for (BookmarkedPost bookmark : bookmarks) {
            // Use the existing findById method from PostRepository
            postRepository.findById(bookmark.getPostId()).ifPresent(posts::add);
        }

        return posts;
    }
}