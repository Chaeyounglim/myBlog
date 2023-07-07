package com.sparta.myblog.controller;

import com.sparta.myblog.dto.CommentRequestDto;
import com.sparta.myblog.dto.CommentResponseDto;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.service.CommentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{post_id}/comment")
    public CommentResponseDto createComment(
            @PathVariable Long post_id,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(post_id, requestDto, userDetails.getUser());
    }


    @PutMapping("/post/comment/{comment_id}")
    public CommentResponseDto updateComment(
            @PathVariable Long comment_id,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletResponse response) throws IOException {
        return commentService.updateComment(response,comment_id,requestDto,userDetails.getUser());
    }


    @DeleteMapping("/post/comment/{comment_id}")
    public void updateComment(
            @PathVariable Long comment_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletResponse res) throws IOException {
        commentService.deleteComment(res,comment_id,userDetails.getUser());
    }
}
