package com.sparta.myblog.service;

import com.sparta.myblog.dto.PostRequestDto;
import com.sparta.myblog.dto.PostResponseDto;
import com.sparta.myblog.entity.Post;
import com.sparta.myblog.entity.User;
import com.sparta.myblog.repository.PostRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public List<PostResponseDto> getPostList() {
        return postRepository.findAllByOrderByCreateAtDesc().stream().map(PostResponseDto::new).toList();
    }

    public PostResponseDto getPost(Long id) {
        Post post = findPost(id);
        return new PostResponseDto(post);
    }


    public PostResponseDto createPost(PostRequestDto requestDto, User user) {

        Post post = postRepository.save(new Post(requestDto,user));
        return new PostResponseDto(post);
    }


    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {

        // 1. 해당 게시글이 있는지 확인
        Post post = this.findPost(id);

        // 2. 해당 게시글의 작성자라면 수정하도록 함.
        String postUsername = post.getUser().getUsername(); // 게시글의 작성자 이름
        String loginUsername = user.getUsername(); // 로그인된 사용자 이름

        if(postUsername.equals(loginUsername)){
            post.update(requestDto);
        }
        return new PostResponseDto(post);
    }

    public void deletePost(HttpServletResponse res, Long id, User user) throws IOException {
        // 1. 해당 게시글이 있는지 확인
        Post post = this.findPost(id);

        // 2. 해당 게시글의 작성자라면 수정하도록 함.
        String postUsername = post.getUser().getUsername(); // 게시글의 작성자 이름
        String loginUsername = user.getUsername(); // 로그인된 사용자 이름

        if(postUsername.equals(loginUsername)){
            postRepository.delete(post);
            this.responseResult(res,200,"게시글 삭제 성공");
        }else {
            this.responseResult(res,401,"게시글 삭제 실패");
        }
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
    }

    private void responseResult(HttpServletResponse response, int statusCode, String message) throws IOException {
        String jsonResponse = "{\"status\": " + statusCode + ", \"message\": \"" + message + "\"}";

        // Content-Type 및 문자 인코딩 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // PrintWriter 를 사용하여 응답 데이터 전송
        PrintWriter writer = response.getWriter();
        writer.write(jsonResponse);
        writer.flush();
    }
}
