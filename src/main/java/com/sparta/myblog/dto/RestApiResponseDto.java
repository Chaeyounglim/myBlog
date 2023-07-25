package com.sparta.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestApiResponseDto {

    private int statusCode;
    private String resultMessage;
    private Object result;
}
