
package com.sparta.myblog.controller;

import com.sparta.myblog.dto.PostRequestDto;
import com.sparta.myblog.dto.PostResponseDto;
import com.sparta.myblog.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api")
@RestController
public class PostController {
    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    // 전체 게시글 목록 조회
    @GetMapping("/posts")
    public List<PostResponseDto> getPostList() {
        return postService.getPostList();
    }

    // 선택한 게시글 조회
    @GetMapping("/posts/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    // 게시글 작성
    @PostMapping("/posts")
    public PostResponseDto createPost(@RequestBody PostRequestDto postRequestDto){
        return postService.createPost(postRequestDto);
    }

    // 선택한 게시글 수정
    @PutMapping("/posts/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto){
        return postService.updatePost(id, postRequestDto);
    }

    // 선택한 게시글 삭제
    @DeleteMapping("/posts/{id}")
    public boolean deletePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto){
        return postService.deletePost(id, postRequestDto);
    }

}
