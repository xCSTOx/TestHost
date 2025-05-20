package com.example.community.entity;

import com.example.userregistration.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookmarked_post")
@IdClass(BookmarkedPostId.class)
public class BookmarkedPost {
    @Id
    @Column(name = "post_id", length = 6, nullable = false, unique = true)
    private String postId;

    @Id
    @Column(name = "user_id", length = 6, nullable = false, unique = true)
    private String userId;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    @JsonBackReference
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonBackReference
    private User user;
}