package com.sparta.myblog.controller;


import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.dto.UserRequestDto;
import com.sparta.myblog.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final MessageSource messageSource;

    // 회원가입
    @PostMapping("/user/signup")
    public void signUp(@Valid @RequestBody UserRequestDto userRequestDto, HttpServletResponse res) throws IOException {
        userService.signUp(userRequestDto,res);
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<RestApiResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        RestApiResponseDto restApiException = new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(),ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(),null );
        // 위 문장에 check 하고 디버깅 하면 defaultMessage 위치를 알 수 있음.
        return new ResponseEntity<>(
                // HTTP body
                restApiException,
                // HTTP status code
                HttpStatus.BAD_REQUEST
        );
    }

}
