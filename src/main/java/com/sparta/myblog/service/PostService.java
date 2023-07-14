package com.sparta.myblog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myblog.dto.PostRequestDto;
import com.sparta.myblog.dto.PostResponseDto;
import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.entity.*;
import com.sparta.myblog.exception.PostNotFoundException;
import com.sparta.myblog.repository.CommentRepository;
import com.sparta.myblog.repository.PostLikeRepository;
import com.sparta.myblog.repository.PostRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MessageSource messageSource;
    private final PostLikeRepository postLikeRepository;


    // 1. 전체 게시글 모두 조회
    public List<PostResponseDto> getPostList() {
        // 게시글 리스트 변수 선언
        List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();
        List<PostResponseDto> responseDtoList = new ArrayList<>();

        // 각 게시글 별 댓글 리스트 set 하기
        // Post 를 PostResponseDto 타입으로 변환하기
        for (Post post : postList) {
            // 최신 댓글 순으로 출력하기 위함.
            PostResponseDto postResponseDto = new PostResponseDto(post);
            if (postResponseDto.getCommentResponseDtoList().size() > 0) { // 해당 게시글에 댓글이 있을 경우 내림차순 정렬
                List<Comment> sortedCommentList = commentRepository.findAllByPostIdOrderByCreatedAtDesc(post.getId());
                postResponseDto.setCommentResponseDtoList(sortedCommentList);
            } // the end of if()
            responseDtoList.add(postResponseDto);
        } // the end of for()
        return responseDtoList;
    }


    // 2. 선택한 게시글 한개 조회
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new PostNotFoundException(messageSource.getMessage(
                        "not.found.post",
                        null,
                        "Not Found post",
                        Locale.getDefault()
                ))
        );
        PostResponseDto postResponseDto = new PostResponseDto(post);
        if (postResponseDto.getCommentResponseDtoList().size() > 0) { // 해당 게시글에 댓글이 있을 경우 내림차순 정렬
            List<Comment> sortedCommentList = commentRepository.findAllByPostIdOrderByCreatedAtDesc(post.getId());
            postResponseDto.setCommentResponseDtoList(sortedCommentList);
        } // the end of if()
        return postResponseDto;
    }


    // 3. 게시글 작성
    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = postRepository.save(new Post(requestDto, user));
        log.info("게시글 저장 완료");
        return new PostResponseDto(post);
    }


    // 4. 게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {
        // 1. 해당 게시글이 있는지 확인
        Post post = postRepository.findById(id).orElseThrow(() ->
                new PostNotFoundException(messageSource.getMessage(
                        "not.found.post",
                        null,
                        "Not Found post",
                        Locale.getDefault()
                ))
        );

        // 2. 해당 게시글의 작성자라면 수정하도록 함.
        Long inputId = post.getUser().getId(); // 게시글의 작성자 user_id
        Long loginId = user.getId(); // 로그인된 사용자 user_id

        if (!inputId.equals(loginId) && !user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException(
                    messageSource.getMessage(
                            "no.match.user",
                            null,
                            "No Match User",
                            Locale.getDefault() // 국제화하는 것임.
                    )
            );
        }// the end of if()

        post.update(requestDto);
        log.info("게시글 수정 완료");
        return new PostResponseDto(post);
    }


    // 5. 게시글 삭제
    public void deletePost(HttpServletResponse res, Long id, User user) throws IOException {
        // 1. 해당 게시글이 있는지 확인
        Post post = postRepository.findById(id).orElseThrow(() ->
                new PostNotFoundException(messageSource.getMessage(
                        "not.found.post",
                        null,
                        "Not Found post",
                        Locale.getDefault()
                ))
        );

        // 2. 해당 게시글의 작성자라면 수정하도록 함.
        Long inputId = post.getUser().getId();// 게시글의 user_id
        Long loginId = user.getId(); // 로그인된 user_id

        if (!inputId.equals(loginId) && !user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException(
                    messageSource.getMessage(
                            "no.match.user",
                            null,
                            "No Match User",
                            Locale.getDefault() // 국제화하는 것임.
                    )
            );
        }// the end of if()

        // 해당 게시글에 해당하는 좋아요 데이터 삭제
        List<PostLike> likeList = postLikeRepository.findByPostId(id);
        postLikeRepository.deleteAll(likeList);

        // 해당 게시글 삭제
        postRepository.delete(post);
        this.responseResult(res, HttpStatus.OK, "게시글 삭제 성공");
        log.info("게시글 삭제 완료");
    }



    private void responseResult(HttpServletResponse res, HttpStatus status, String message) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        RestApiResponseDto dto = new RestApiResponseDto(status.value(), message);
        ObjectMapper objectMapper = new ObjectMapper();
        res.getWriter().write(objectMapper.writeValueAsString(dto));
    }


}
