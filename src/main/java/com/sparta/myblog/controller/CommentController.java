package com.sparta.myblog.controller;

import com.sparta.myblog.dto.CommentRequestDto;
import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.exception.TokenNotValidateException;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.service.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentServiceImpl commentService;

    @PostMapping("/comments")
    public ResponseEntity<RestApiResponseDto> createComment(
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        this.tokenValidate(userDetails);
        return commentService.createComment(requestDto, userDetails.getUser());
    }


    @PutMapping("/comments/{comment_id}")
    public ResponseEntity<RestApiResponseDto> updateComment(
            @PathVariable Long comment_id,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        this.tokenValidate(userDetails);
        return commentService.updateComment(comment_id,requestDto,userDetails.getUser());
    }


    @DeleteMapping("/comments/{comment_id}")
    public ResponseEntity<RestApiResponseDto> updateComment(
            @PathVariable Long comment_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        this.tokenValidate(userDetails);
        return commentService.deleteComment(comment_id,userDetails.getUser());
    }

    public void tokenValidate(UserDetailsImpl userDetails) {
        try{
            userDetails.getUser();
        }catch (Exception ex){
            throw new TokenNotValidateException("토큰이 유효하지 않습니다.");
        }
    }

}
