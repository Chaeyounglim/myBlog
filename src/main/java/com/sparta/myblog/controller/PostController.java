
package com.sparta.myblog.controller;

import com.sparta.myblog.dto.PostRequestDto;
import com.sparta.myblog.dto.PostResponseDto;
import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.entity.Post;
import com.sparta.myblog.exception.TokenNotValidateException;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;


@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostServiceImpl postService;

    // 전체 게시글 목록 조회
    @GetMapping("/posts")
    public List<PostResponseDto> getPostList() {
        return postService.getPostList();
    }


    // 선택한 게시글 상세 조회
    @GetMapping("/posts/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }


    // 게시글 작성
    @PostMapping("/posts")
    public ResponseEntity<RestApiResponseDto> createPost(
            @RequestBody PostRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        this.tokenValidate(userDetails);
        return postService.createPost(requestDto, userDetails.getUser());
    }


    // 선택한 게시글 수정
    @PutMapping("/posts/{id}")
    public ResponseEntity<RestApiResponseDto> updatePost(
            @PathVariable Long id,
            @RequestBody PostRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try{
            Post post = postService.findByPost(id);
            return postService.updatePost(post, requestDto, userDetails.getUser());
        }catch (RejectedExecutionException e){
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(),"작성자만 수정 할 수 있습니다.",null));
        }
    }


    // 선택한 게시글 삭제
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<RestApiResponseDto> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try{
            Post post = postService.findByPost(id);
            return postService.deletePost(post, userDetails.getUser());
        }catch (RejectedExecutionException e){
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(),"작성자만 삭제 할 수 있습니다.",null));
        }
    }

    public void tokenValidate(UserDetailsImpl userDetails) {
        try{
            userDetails.getUser();
        }catch (Exception ex){
            throw new TokenNotValidateException("토큰이 유효하지 않습니다.");
        }
    }

}
