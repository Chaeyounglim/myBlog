package com.sparta.myblog.service;

import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface CommentLikeService {

    /**
     * 댓글 좋아요 증가
     * @param id 댓글 식별자
     * @param user 댓글 입력한 유저
     * @return 상태 코드 및 메시지
     */
    ResponseEntity<RestApiResponseDto> increaseLike(Long id, User user);


    /**
     * 댓글 좋아요 취소
     * @param id 댓글 식별자
     * @param user 댓글 입력한 유저
     * @return 상태 코드 및 메시지
     */
    ResponseEntity<RestApiResponseDto> decreaseLike(Long id, User user);


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
