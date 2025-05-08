package com.example.breakfreeBE.community.dto;

import lombok.Data;
import com.example.breakfreeBE.community.entity.Comment;
import java.util.List;

@Data
public class PostDetailDTO {
    private PostDTO post;
    private List<CommentDTO> commentsu;
}
