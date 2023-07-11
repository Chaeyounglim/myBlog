package com.sparta.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestApiResponseDto {

    private String errorMessage;
    private int statusCode;
}
