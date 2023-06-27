package com.sparta.myblog.controller;

import com.sparta.myblog.dto.CommentRequestDto;
import com.sparta.myblog.dto.CommentResponseDto;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.service.CommentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    //1. 댓글 수정 삭제 부분 - /api/post/{id}/comment/{id}
    //id를 구분을 줘야 할거 같아요!
    //
    //
    //2. 그리고 생각해보니까 저희 게시글 삭제할 때
    //해당 게시글에 속한 댓글도 삭제하게끔 cascade 해줘야 할 것 같더라구요
    //
    //3. 아 참 댓글 작성, 수정 부분에서 response를 댓글 테이블에서
    //id, 작성자, 작성일자, 수정일자, 내용 다 넘겨줘야 하지 않을까 싶어요!


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
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(comment_id,requestDto,userDetails.getUser());
    }


    @DeleteMapping("/post/comment/{comment_id}")
    public void updateComment(
            @PathVariable Long comment_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletResponse res) throws IOException {
        commentService.deleteComment(res,comment_id,userDetails.getUser());
    }
}
