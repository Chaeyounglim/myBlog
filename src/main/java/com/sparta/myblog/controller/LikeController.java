package com.sparta.myblog.controller;

import com.sparta.myblog.dto.LikeRequestDto;
import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/post/{post_id}/like")
    public ResponseEntity<RestApiResponseDto> like(
            @PathVariable Long post_id,
            @RequestBody LikeRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.like(post_id,requestDto,userDetails.getUser());
    }
}
