package com.sparta.myblog.dto;

import com.sparta.myblog.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private Long id;
    private String title;
    private String name;
    private String contents;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.name = post.getName();
        this.contents = post.getContents();
        this.password = post.getPassword();
        this.createdAt = post.getCreateAt();
        this.modifiedAt = post.getModifiedAt();
    }
}
