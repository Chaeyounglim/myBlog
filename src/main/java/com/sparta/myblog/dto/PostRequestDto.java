package com.sparta.myblog.dto;

import lombok.Getter;

@Getter
public class PostRequestDto {
    private String title;
    private String name;
    private String contents;
    private String password;
}
