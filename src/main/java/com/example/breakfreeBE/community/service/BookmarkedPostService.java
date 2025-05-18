package com.example.breakfreeBE.community.service;

import com.example.breakfreeBE.community.dto.BookmarkedRequestDTO;
import com.example.breakfreeBE.community.dto.PostDTO;
import com.example.breakfreeBE.community.entity.BookmarkedPost;
import com.example.breakfreeBE.community.entity.Post;
import com.example.breakfreeBE.community.repository.BookmarkedPostRepository;
import com.example.breakfreeBE.community.repository.PostRepository;
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

    @Autowired
    private PostService postService;

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

    public List<PostDTO> viewBookmarkedPosts(String userId) {
        List<BookmarkedPost> bookmarks = bookmarkedPostRepository.findByUserId(userId);
        List<PostDTO> result = new ArrayList<>();

        for (BookmarkedPost bookmark : bookmarks) {
            postRepository.findById(bookmark.getPostId()).ifPresent(post -> {
                PostDTO dto = postService.convertToDTOP(post); // <-- pakai convert dari PostService
                if (dto.isBookmarked()) { // filter yang benar-benar bookmarked
                    result.add(dto);
                }
            });
        }

        return result;
    }
}
