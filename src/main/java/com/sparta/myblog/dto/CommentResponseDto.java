package com.sparta.myblog.dto;

import com.sparta.myblog.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String contents;
    private String username;
    private Long post_id;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int likeCnt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.username = comment.getUser().getUsername();
        this.post_id = comment.getPost().getId();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.likeCnt = comment.getCommentLikeList().size();
    }

}
