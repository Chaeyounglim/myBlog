package com.sparta.myblog.service;

import com.sparta.myblog.dto.CommentRequestDto;
import com.sparta.myblog.dto.CommentResponseDto;
import com.sparta.myblog.entity.*;
import com.sparta.myblog.exception.CommentNotFoundException;
import com.sparta.myblog.exception.PostNotFoundException;
import com.sparta.myblog.repository.CommentLikeRepository;
import com.sparta.myblog.repository.CommentRepository;
import com.sparta.myblog.repository.PostRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MessageSource messageSource;


    // 댓글 작성하기
    public CommentResponseDto createComment
    (Long postId, CommentRequestDto requestDto, User user) {
        // 해당 게시글이 있는지
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException(messageSource.getMessage(
                        "not.found.post",
                        null,
                        "Not Found post",
                        Locale.getDefault()
                ))
        );

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
            HttpServletResponse response, Long commentId, CommentRequestDto requestDto, User user) throws IOException {
        // 1. 해당하는 댓글 가져오기
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException(messageSource.getMessage(
                        "not.found.comment",
                        null,
                        "Not Found comment",
                        Locale.getDefault()
                ))
        );

        // 2. 댓글 작성자 이름 가져오기
        Long commentUserId = comment.getUser().getId();

        // 3. 로그인한 유저가 해당 댓글 작성자인지 확인하기
        if (!commentUserId.equals(user.getId()) && !user.getRole().equals(UserRoleEnum.ADMIN)) { // 작성자가 아닐 경우
            log.error("댓글 작성자가 아닌 사용자가 댓글 수정 요청");
            throw new IllegalArgumentException(
                    messageSource.getMessage(
                            "no.match.user",
                            null,
                            "No Match User",
                            Locale.getDefault() // 국제화하는 것임.
                    )
            );
        } // the end of if()

        // 4. 해당 댓글 수정하기
        comment.update(requestDto);
        log.info("댓글 수정 완료");
        return new CommentResponseDto(comment);
    }


    // 댓글 삭제하기
    public void deleteComment(HttpServletResponse res, Long commentId, User user) throws IOException {
        // 1. 해당하는 댓글 가져오기
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException(messageSource.getMessage(
                        "not.found.comment",
                        null,
                        "Not Found comment",
                        Locale.getDefault()
                ))
        );

        // 2. 댓글 작성자 이름 가져오기
        Long commentUserId = comment.getUser().getId();

        // 3. 로그인한 유저가 해당 댓글 작성자인지 확인하기
        if (!commentUserId.equals(user.getId()) && !user.getRole().equals(UserRoleEnum.ADMIN)) { // 작성자가 아닐 경우
            log.error("댓글 작성자가 아닌 사용자가 댓글 삭제 요청");
            throw new IllegalArgumentException(
                    messageSource.getMessage(
                            "no.match.user",
                            null,
                            "No Match User",
                            Locale.getDefault() // 국제화하는 것임.
                    )
            );
        } // the end of if()

        // 4. 해당 댓글 삭제하기
        this.deleteLike(commentId); // 해당 댓글에 해당하는 좋아요 데이터 삭제
        commentRepository.delete(comment);
        responseResult(res, 200, "댓글 삭제 성공");
        log.info("댓글 삭제 완료");
    }

    private void deleteLike(Long commentId) {
        List<CommentLike> likeList = commentLikeRepository.findByCommentId(commentId);
        commentLikeRepository.deleteAll(likeList);
    }


    // Client 에 HttpServletResponse 를 통해 반환할 msg, status 세팅 메서드
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