
package com.sparta.myblog.controller;

import com.sparta.myblog.dto.PostRequestDto;
import com.sparta.myblog.dto.PostResponseDto;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.service.PostService;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

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
/*    @PostMapping("/posts")
    public PostResponseDto createPost(
            @RequestBody PostRequestDto postRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.createPost(postRequestDto,userDetails.getUser());
    }*/

    // 선택한 게시글 수정
    @PutMapping("/posts/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto){
        return postService.updatePost(id, postRequestDto);
    }

    // 선택한 게시글 삭제
    @DeleteMapping("/posts/{id}")
    public boolean deletePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto){
        return postService.deletePost(id, postRequestDto.getPassword());
    }

}
