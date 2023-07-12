package com.sparta.myblog.controller;

import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostLikeController {

    private final PostLikeService likeService;

    @PostMapping("/post/{post_id}/like")
    public ResponseEntity<RestApiResponseDto> increaseLike(
            @PathVariable Long post_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.increaseLike(post_id,userDetails.getUser());
    }

    @PutMapping("/post/{post_id}/like")
    public ResponseEntity<RestApiResponseDto> decreaseLike(
            @PathVariable Long post_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.decreaseLike(post_id,userDetails.getUser());
    }


}
