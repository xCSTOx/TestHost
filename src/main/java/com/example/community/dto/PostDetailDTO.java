package com.example.community.dto;

import lombok.Data;
import com.example.community.entity.Comment;
import java.util.List;

@Data
public class PostDetailDTO {
    private PostDTO post;
    private List<CommentDTO> commentsu;
}
