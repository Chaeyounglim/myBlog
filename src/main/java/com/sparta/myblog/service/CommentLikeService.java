package com.sparta.myblog.service;

import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.entity.Comment;
import com.sparta.myblog.entity.CommentLike;
import com.sparta.myblog.entity.User;
import com.sparta.myblog.exception.CommentNotFoundException;
import com.sparta.myblog.exception.PostNotFoundException;
import com.sparta.myblog.repository.CommentLikeRepository;
import com.sparta.myblog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final MessageSource messageSource;


    @Transactional
    public ResponseEntity<RestApiResponseDto> increaseLike(Long id, User user) {
        // 1. 댓글 가져오기

        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new CommentNotFoundException(messageSource.getMessage(
                        "not.found.comment",
                        null,
                        "Not Found comment",
                        Locale.getDefault()
                ))
        );

        // 2. 댓글 데이터가 있을 경우
        Optional<CommentLike> checkLike = likeRepository.findByUserIdAndCommentId(user.getId(), comment.getId());

        if(checkLike.isPresent()){
            // 2-1. 댓글 데이터가 true 일 경우
            if(checkLike.get().isLiked()) {
                throw new IllegalArgumentException(
                        messageSource.getMessage(
                                "duplicated.increase.like",
                                null,
                                "duplicated increase like",
                                Locale.getDefault() // 국제화하는 것임.
                        )
                );
            }
            // 2-2. 댓글 데이터가 false 일 경우
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
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new CommentNotFoundException(messageSource.getMessage(
                        "not.found.comment",
                        null,
                        "Not Found comment",
                        Locale.getDefault()
                ))
        );

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
                throw new IllegalArgumentException(
                        messageSource.getMessage(
                                "duplicated.decrease.like",
                                null,
                                "duplicated decrease like",
                                Locale.getDefault() // 국제화하는 것임.
                        )
                );
            }
        }else {
            throw new IllegalArgumentException(
                    messageSource.getMessage(
                            "not.found.like",
                            null,
                            "not found like",
                            Locale.getDefault() // 국제화하는 것임.
                    )
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
