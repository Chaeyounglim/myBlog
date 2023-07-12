package com.sparta.myblog.service;

import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.entity.User;
import com.sparta.myblog.repository.CommentLikeRepository;
import com.sparta.myblog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository likeRepository;
    private final CommentRepository commentRepository;


    public ResponseEntity<RestApiResponseDto> increaseLike(Long commentId, User user) {

    }

    public ResponseEntity<RestApiResponseDto> decreaseLike(Long commentId, User user) {
    }
}
