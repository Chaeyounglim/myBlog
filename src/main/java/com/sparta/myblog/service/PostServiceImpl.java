package com.sparta.myblog.service;

import com.sparta.myblog.dto.PostRequestDto;
import com.sparta.myblog.dto.PostResponseDto;
import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.entity.*;
import com.sparta.myblog.exception.PostNotFoundException;
import com.sparta.myblog.repository.CommentRepository;
import com.sparta.myblog.repository.PostLikeRepository;
import com.sparta.myblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;


    // 1. 전체 게시글 모두 조회
    @Override
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
    @Override
    public PostResponseDto getPost(Long id) {
        Post post = findByPost(id);

        PostResponseDto postResponseDto = new PostResponseDto(post);
        if (postResponseDto.getCommentResponseDtoList().size() > 0) { // 해당 게시글에 댓글이 있을 경우 내림차순 정렬
            List<Comment> sortedCommentList = commentRepository.findAllByPostIdOrderByCreatedAtDesc(post.getId());
            postResponseDto.setCommentResponseDtoList(sortedCommentList);
        } // the end of if()
        return postResponseDto;
    }


    // 3. 게시글 작성
    @Override
    public ResponseEntity<RestApiResponseDto> createPost(PostRequestDto requestDto, User user) {
        Post post = postRepository.save(new Post(requestDto, user));
        return getRestApiResponseDtoResponseEntity( "게시글 작성 성공",HttpStatus.OK,new PostResponseDto(post));
    }


    // 4. 게시글 수정
    @Transactional
    @Override
    public ResponseEntity<RestApiResponseDto> updatePost(Post post,PostRequestDto requestDto, User user) {
        post.update(requestDto);
        return getRestApiResponseDtoResponseEntity( "게시글 수정 성공",HttpStatus.OK,new PostResponseDto(post));
    }


    // 5. 게시글 삭제
    @Override
    public ResponseEntity<RestApiResponseDto> deletePost(Post post, User user) {
        postRepository.delete(post);
        return getRestApiResponseDtoResponseEntity( "게시글 삭제 성공",HttpStatus.OK,null);
    }



    public Post findByPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new PostNotFoundException("해당 게시글이 존재하지 않습니다."));
    }

    @Override
    public ResponseEntity<RestApiResponseDto> getRestApiResponseDtoResponseEntity(
            String message, HttpStatus status, Object result) {
        RestApiResponseDto restApiResponseDto = new RestApiResponseDto(status.value(),message,result);
        return new ResponseEntity<>(
                restApiResponseDto,
                status
        );
    }


}
