package com.sparta.myblog.dto;

import com.sparta.myblog.entity.Comment;
import com.sparta.myblog.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostResponseDto {

    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int likeCnt;
    private List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUser().getUsername();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.likeCnt = post.getPostLikeList().size();
        if(post.getCommentList().size()>0) {
            for (Comment comment : post.getCommentList()) {
                this.commentResponseDtoList.add(new CommentResponseDto(comment));
            }
        }// end of the if()
    }// end of constructor method()

    public void setCommentResponseDtoList(List<Comment> sortedCommentList) {
        this.getCommentResponseDtoList().clear();
        for (Comment comment : sortedCommentList) {
            this.commentResponseDtoList.add(new CommentResponseDto(comment));
        }
    }
 }
