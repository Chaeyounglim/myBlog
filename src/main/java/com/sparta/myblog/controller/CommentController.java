package com.sparta.myblog.controller;

import com.sparta.myblog.dto.CommentRequestDto;
import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.entity.Comment;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.service.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.RejectedExecutionException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentServiceImpl commentService;

    @PostMapping("/comments")
    public ResponseEntity<RestApiResponseDto> createComment(
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(requestDto, userDetails.getUser());
    }


    @PutMapping("/comments/{id}")
    public ResponseEntity<RestApiResponseDto> updateComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Comment comment = commentService.findById(id);
            log.info("controller" + String.valueOf(comment.getId()));
            return commentService.updateComment(comment,requestDto,userDetails.getUser());
        }catch (RejectedExecutionException e){
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(),"작성자만 수정 할 수 있습니다.",null));
        }
    }


    @DeleteMapping("/comments/{id}")
    public ResponseEntity<RestApiResponseDto> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try{
            log.info("controller");
            Comment comment = commentService.findById(id);
            log.info(String.valueOf(comment.getUser().getId()));
            return commentService.deleteComment(comment,userDetails.getUser());
        }catch (RejectedExecutionException e){
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(),"작성자만 삭제 할 수 있습니다.",null));
        }
    }


}
