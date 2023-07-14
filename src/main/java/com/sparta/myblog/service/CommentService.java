package com.sparta.myblog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myblog.dto.CommentRequestDto;
import com.sparta.myblog.dto.CommentResponseDto;
import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.entity.*;
import com.sparta.myblog.exception.CommentNotFoundException;
import com.sparta.myblog.exception.PostNotFoundException;
import com.sparta.myblog.repository.CommentLikeRepository;
import com.sparta.myblog.repository.CommentRepository;
import com.sparta.myblog.repository.PostRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    // 댓글 작성하기
    public CommentResponseDto createComment
    (Long postId, CommentRequestDto requestDto, User user) {
        // 해당 게시글이 있는지
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException("해당 게시글이 존재하지 않습니다."));

        // DB에 댓글 저장하기.
        Comment comment = new Comment(requestDto);
        comment.setPost(post);
        comment.setUser(user);

        commentRepository.save(comment);

        post.addComment(comment);

        log.info("댓글 저장 완료");
        return new CommentResponseDto(comment);
    }


    // 댓글 수정하기
    @Transactional
    public CommentResponseDto updateComment(
            HttpServletResponse res, Long commentId, CommentRequestDto requestDto, User user) throws IOException {
        // 1. 해당하는 댓글 가져오기
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException("해당 댓글이 존재하지 않습니다."));

        // 2. 댓글 작성자 이름 가져오기
        Long commentUserId = comment.getUser().getId();

        // 3. 로그인한 유저가 해당 댓글 작성자인지 확인하기
        if (!commentUserId.equals(user.getId()) && !user.getRole().equals(UserRoleEnum.ADMIN)) { // 작성자가 아닐 경우
            log.error("댓글 작성자가 아닌 사용자가 댓글 수정 요청");
            throw new IllegalArgumentException("작성자 혹은 관리자만 삭제/수정 할 수 있습니다.");
        } // the end of if()
        responseResult(res, HttpStatus.OK, "댓글 수정 성공");

        // 4. 해당 댓글 수정하기
        comment.update(requestDto);
        log.info("댓글 수정 완료");
        return new CommentResponseDto(comment);
    }


    // 댓글 삭제하기
    public void deleteComment(HttpServletResponse res, Long commentId, User user) throws IOException {
        // 1. 해당하는 댓글 가져오기
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException("해당 댓글이 존재하지 않습니다."));

        // 2. 댓글 작성자 이름 가져오기
        Long commentUserId = comment.getUser().getId();

        // 3. 로그인한 유저가 해당 댓글 작성자인지 확인하기
        if (!commentUserId.equals(user.getId()) && !user.getRole().equals(UserRoleEnum.ADMIN)) { // 작성자가 아닐 경우
            log.error("댓글 작성자가 아닌 사용자가 댓글 삭제 요청");
            throw new IllegalArgumentException("작성자 혹은 관리자만 삭제/수정 할 수 있습니다.");
        } // the end of if()

        // 4. 해당 댓글에 해당하는 좋아요 데이터 삭제
        List<CommentLike> likeList = commentLikeRepository.findByCommentId(commentId);
        commentLikeRepository.deleteAll(likeList);

        // 5. 해당 댓글 삭제하기
        commentRepository.delete(comment);
        responseResult(res, HttpStatus.OK, "댓글 삭제 성공");

        log.info("댓글 삭제 완료");
    }


    // Client 에 HttpServletResponse 를 통해 반환할 msg, status 세팅 메서드
    private void responseResult(HttpServletResponse res, HttpStatus status,  String message) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        RestApiResponseDto dto = new RestApiResponseDto(status.value(), message);
        ObjectMapper objectMapper = new ObjectMapper();
        res.getWriter().write(objectMapper.writeValueAsString(dto));
    }

}