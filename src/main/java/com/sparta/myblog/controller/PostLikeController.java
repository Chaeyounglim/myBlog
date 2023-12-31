package com.sparta.myblog.controller;

import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.exception.TokenNotValidateException;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.service.PostLikeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostLikeController {

    private final PostLikeServiceImpl likeService;

    @PostMapping("/posts/{post_id}/like")
    public ResponseEntity<RestApiResponseDto> increaseLike(
            @PathVariable Long post_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        this.tokenValidate(userDetails);
        return likeService.increaseLike(post_id,userDetails.getUser());
    }


    @PutMapping("/posts/{post_id}/dislike")
    public ResponseEntity<RestApiResponseDto> decreaseLike(
            @PathVariable Long post_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        this.tokenValidate(userDetails);
        return likeService.decreaseLike(post_id,userDetails.getUser());
    }

    public void tokenValidate(UserDetailsImpl userDetails) {
        try{
            userDetails.getUser();
        }catch (Exception ex){
            throw new TokenNotValidateException("토큰이 유효하지 않습니다.");
        }
    }

}
