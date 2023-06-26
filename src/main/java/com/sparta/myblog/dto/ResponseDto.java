package com.sparta.myblog.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto {
    // 메시지와 상태 코드 반환하기.
    private String msg;
    private int status;

    public ResponseDto(String msg, int status) {
        this.msg = msg;
        this.status = status;
    }

    public ResponseDto() {

    }
}
