package com.sparta.myblog.service;

import com.sparta.myblog.dto.UserRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public interface UserService {


    /**
     * 회원가입 메서드
     * @param requestDto 회원가입 요청한 데이터
     * @param res HttpServletResponse 객체
     * @throws IOException
     */
    void signUp(UserRequestDto requestDto, HttpServletResponse res) throws IOException;


    /**
     * 반환 메소드
     * @param res HttpServletResponse 객체
     * @param status 응답 코드
     * @param message 반환할 메시지
     * @throws IOException
     */
    void responseResult(HttpServletResponse res, HttpStatus status, String message) throws IOException;
}
