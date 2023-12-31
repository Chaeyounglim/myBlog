package com.sparta.myblog.controller;


import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.exception.TokenNotValidateException;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.service.CommentLikeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentLikeController {

    private final CommentLikeServiceImpl commentLikeService;

    @PostMapping("/comments/{comment_id}/like")
    public ResponseEntity<RestApiResponseDto> increaseLike(
            @PathVariable Long comment_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        this.tokenValidate(userDetails);
        return commentLikeService.increaseLike(comment_id,userDetails.getUser());
    }

    @PutMapping("/comments/{comment_id}/dislike")
    public ResponseEntity<RestApiResponseDto> decreaseLike(
            @PathVariable Long comment_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        this.tokenValidate(userDetails);
        return commentLikeService.decreaseLike(comment_id,userDetails.getUser());
    }

    public void tokenValidate(UserDetailsImpl userDetails) {
        try{
            userDetails.getUser();
        }catch (Exception ex){
            throw new TokenNotValidateException("토큰이 유효하지 않습니다.");
        }
    }
}
