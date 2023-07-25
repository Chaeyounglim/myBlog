package com.sparta.myblog.service;

import com.sparta.myblog.dto.PostRequestDto;
import com.sparta.myblog.dto.PostResponseDto;
import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.entity.Post;
import com.sparta.myblog.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PostService {

    /**
     * 모든 게시글 정보 가져오기
     * @return 게시글에 대한 데이터 모두 가져오기
     */
    List<PostResponseDto> getPostList();


    /**
     * 게시글 단건 조회하기
     * @param id 조회할 게시글 id
     * @return 조회한 1개의 게시글 데이터
     */
    PostResponseDto getPost(Long id);


    /**
     * 게시글 작성
     * @param requestDto 입력한 게시글 정보
     * @param user 게시글을 작성한 유저
     * @return 상태 코드 및 메시지, 작성한 게시글 데이터
     */
    ResponseEntity<RestApiResponseDto> createPost(PostRequestDto requestDto, User user);


    /**
     * 게시글 수정
     * @param post 수정 요청한 게시글
     * @param requestDto 수정할 내용
     * @param user 수정 요청한 유저
     * @return 상태 코드 및 메시지, 수정한 게시글 데이터
     */
    ResponseEntity<RestApiResponseDto> updatePost(Post post, PostRequestDto requestDto, User user);


    /**
     * 게시글 삭제
     * @param post 삭제할 게시글
     * @param user 삭제 요청한 유저
     * @return 상태 코드 및 메시지
     */
    ResponseEntity<RestApiResponseDto> deletePost(Post post, User user);

    /**
     * 반환 메소드
     * @param message 반환할 메시지
     * @param status 응답 코드
     * @param result 응답 데이터
     * @return 위의 정보들을 ResponseEntity 클래스에 담아서 반환
     */
    ResponseEntity<RestApiResponseDto> getRestApiResponseDtoResponseEntity(
            String message, HttpStatus status, Object result);
}
