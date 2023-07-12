package com.sparta.myblog.service;

import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.entity.Comment;
import com.sparta.myblog.entity.CommentLike;
import com.sparta.myblog.entity.User;
import com.sparta.myblog.repository.CommentLikeRepository;
import com.sparta.myblog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository likeRepository;
    private final CommentRepository commentRepository;


    @Transactional
    public ResponseEntity<RestApiResponseDto> increaseLike(Long id, User user) {
        // 1. 댓글 가져오기
        Optional<Comment> checkComment = commentRepository.findById(id);

        if(!checkComment.isPresent()){
            return getRestApiResponseDtoResponseEntity(
                    "해당 댓글이 없습니다.", HttpStatus.NOT_FOUND);
        }

        Comment comment = checkComment.get();

        // 2. 댓글 데이터가 있을 경우
        Optional<CommentLike> checkLike = likeRepository.findByUserIdAndCommentId(user.getId(), comment.getId());
        if(checkLike.isPresent()){
            // 2-1. 댓글 데이터가 true 일 경우
            if(checkLike.get().isLiked()){
                return getRestApiResponseDtoResponseEntity(
                        "좋아요 실패 : 이미 눌러져 있습니다.",HttpStatus.BAD_REQUEST);
            }// 2-2. 댓글 데이터가 false 일 경우
            else {
                CommentLike like = checkLike.get();
                like.changeLiked();
                comment.increaseLike();
            }
        }// 3. 댓글 데이터가 없을 경우
        else {
            CommentLike like = new CommentLike(user,comment);
            likeRepository.save(like);
            comment.increaseLike();
        }

        return getRestApiResponseDtoResponseEntity(
                "좋아요 성공", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<RestApiResponseDto> decreaseLike(Long id, User user) {
        // 1. 해당 댓글이 있는지
        Optional<Comment> checkComment = commentRepository.findById(id);
        if(!checkComment.isPresent()){
            return getRestApiResponseDtoResponseEntity(
                    "해당 댓글이 없습니다.", HttpStatus.NOT_FOUND
            );
        }

        Comment comment = checkComment.get();
        // 2. 좋아요 데이터가 있는지
        Optional<CommentLike> checkLike = likeRepository.findByUserIdAndCommentId(user.getId(),comment.getId());

        if(checkLike.isPresent()){
            // 2-1. true 로 저장되어 있을 경우
            if(checkLike.get().isLiked()){
                CommentLike like = checkLike.get();
                like.changeLiked();
                comment.decreaseLike();
            } // 2-2. false 로 저장되어 있을 경우
            else {
                return getRestApiResponseDtoResponseEntity(
                        "좋아요 취소 실패 : 이미 취소된 좋아요입니다.",HttpStatus.BAD_REQUEST
                );
            }
        }else {
            return getRestApiResponseDtoResponseEntity(
                    "좋아요 취소 실패: 좋아요를 누른 적이 없습니다.", HttpStatus.NOT_FOUND
            );
        }

        return getRestApiResponseDtoResponseEntity(
                "좋아요 취소 성공",HttpStatus.OK);
    }

    private ResponseEntity<RestApiResponseDto> getRestApiResponseDtoResponseEntity(
            String message, HttpStatus status) {
        RestApiResponseDto restApiException = new RestApiResponseDto(message, status.value());
        return new ResponseEntity<>(
                restApiException,
                status
        );
    }

}
