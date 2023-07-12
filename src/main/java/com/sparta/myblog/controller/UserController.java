package com.sparta.myblog.controller;


import com.sparta.myblog.dto.UserRequestDto;
import com.sparta.myblog.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/user/signup")
    public void signUp(@Valid @RequestBody UserRequestDto userRequestDto, BindingResult bindingResult, HttpServletResponse res) throws IOException {
        // 1. Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
        }else {
            // 2. userService 에서 signup 하고
            // 3. HttpServletResponse 에 (msg, status set 하기)
            userService.signUp(userRequestDto,res);
        }
    }

}
