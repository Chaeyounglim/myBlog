package com.sparta.myblog.controller;


import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping("comment/{comment_id}/like")
    public ResponseEntity<RestApiResponseDto> increaseLike(
            @PathVariable Long comment_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentLikeService.increaseLike(comment_id,userDetails.getUser());
    }

    @PutMapping("comment/{comment_id}/like")
    public ResponseEntity<RestApiResponseDto> decreaseLike(
            @PathVariable Long comment_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentLikeService.decreaseLike(comment_id,userDetails.getUser());
    }

}
