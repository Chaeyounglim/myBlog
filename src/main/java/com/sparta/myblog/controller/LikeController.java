package com.sparta.myblog.controller;

import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/post/{post_id}/like")
    public ResponseEntity<RestApiResponseDto> increaseLike(
            @PathVariable Long post_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        //log.info("controller");
        return likeService.increaseLike(post_id,userDetails.getUser());
    }

    @DeleteMapping("/post/{post_id}/like")
    public ResponseEntity<RestApiResponseDto> decreaseLike(
            @PathVariable Long post_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.decreaseLike(post_id,userDetails.getUser());
    }


}
