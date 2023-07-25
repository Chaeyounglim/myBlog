package com.sparta.myblog.service;

import com.sparta.myblog.dto.CommentRequestDto;
import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface CommentService {

    /**
     * 댓글 작성
     * @param requestDto 게시글 id와 입력한 댓글 내용
     * @param user 댓글 입력한 유저
     * @return 상태 코드 및 메시지와 입력한 댓글 데이터
     */
    ResponseEntity<RestApiResponseDto> createComment
            (CommentRequestDto requestDto, User user);

    
    /**
     * 댓글 수정
     * @param commentId 댓글 식별자값
     * @param requestDto 수정하고자 하는 댓글 내용
     * @param user 댓글 수정 요청한 유저
     * @return 상태 코드 및 메시지와 입력한 댓글 데이터
     */
    ResponseEntity<RestApiResponseDto> updateComment(Long commentId, CommentRequestDto requestDto, User user);


    /**
     * 댓글 삭제
     * @param commentId 삭제하고자 하는 댓글 식별자
     * @param user 댓글 삭제 요청한 유저
     * @return 상태 코드 및 메시지와 입력한 댓글 데이터
     */
    ResponseEntity<RestApiResponseDto> deleteComment(Long commentId, User user);


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
