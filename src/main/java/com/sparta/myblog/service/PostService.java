package com.sparta.myblog.service;

import com.sparta.myblog.dto.PostRequestDto;
import com.sparta.myblog.dto.PostResponseDto;
import com.sparta.myblog.entity.Post;
import com.sparta.myblog.entity.User;
import com.sparta.myblog.repository.CommentRepository;
import com.sparta.myblog.repository.PostRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;


    // 1. 전체 게시글 모두 조회
    public List<PostResponseDto> getPostList() {
        // 게시글 리스트 변수 선언
        List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();
        List<PostResponseDto> responseDtoList = new ArrayList<>();

        // 각 게시글 별 댓글 리스트 set 하기
        // Post 를 PostResponseDto 타입으로 변환하기
        for (Post post : postList) {
            post.setCommentList(commentRepository.findAllByPostIdOrderByCreatedAtDesc(post.getId()));
            responseDtoList.add(new PostResponseDto(post));
        }
        return responseDtoList;
    }


    // 2. 선택한 게시글 한개 조회
    public PostResponseDto getPost(Long id) {
        Post post = findPost(id);
        post.setCommentList(commentRepository.findAllByPostIdOrderByCreatedAtDesc(post.getId()));
        return new PostResponseDto(post);
    }


    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = postRepository.save(new Post(requestDto,user));
        log.info("게시글 저장 완료");
        return new PostResponseDto(post);
    }


    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {
        // 1. 해당 게시글이 있는지 확인
        Optional<Post> checkPost = postRepository.findById(id);

        if(!checkPost.isPresent()){ // 1-1. 해당 게시글이 없을 경우 실행
            log.error("수정 요청한 게시글이 없습니다.");
            return null; // 해당 메서드 종료
        }

        // 1-2. 해당 게시글이 있을 경우 실행
        Post post = this.findPost(id);

        // 2. 해당 게시글의 작성자라면 수정하도록 함.
        Long inputId = post.getUser().getId(); // 게시글의 작성자 user_id
        Long loginId = user.getId(); // 로그인된 사용자 user_id

        if(inputId.equals(loginId)){
            post.update(requestDto);
            log.info("게시글 수정 완료");
        }
        return new PostResponseDto(post);
    }

    public void deletePost(HttpServletResponse res, Long id, User user) throws IOException {
        // 1. 해당 게시글이 있는지 확인
        Optional<Post> checkPost = postRepository.findById(id);

        if(!checkPost.isPresent()){ // 1-1. 해당 게시글이 없을 경우 실행
            this.responseResult(res, 400, "게시글 삭제 실패 : 해당 게시글이 없습니다.");
            log.error("삭제 요청한 게시글이 없습니다.");
        }else { // 1-2 해당 게시글이 있을 경우 실행
            Post post = this.findPost(id);

            // 2. 해당 게시글의 작성자라면 수정하도록 함.
            Long inputId = post.getUser().getId();// 게시글의 user_id
            Long loginId = user.getId(); // 로그인된 user_id

            if (inputId.equals(loginId)) {
                postRepository.delete(post);
                this.responseResult(res, 200, "게시글 삭제 성공");
                log.info("게시글 삭제 완료");
            } else {
                this.responseResult(res, 400, "게시글 삭제 실패 : 해당 게시글의 작성자가 아닙니다.");
                log.error("게시글 삭제 실패");
            }
        }

    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
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
