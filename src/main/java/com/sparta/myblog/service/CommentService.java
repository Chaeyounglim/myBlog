package com.sparta.myblog.service;

import com.sparta.myblog.dto.CommentRequestDto;
import com.sparta.myblog.dto.CommentResponseDto;
import com.sparta.myblog.entity.Comment;
import com.sparta.myblog.entity.Post;
import com.sparta.myblog.entity.User;
import com.sparta.myblog.repository.CommentRepository;
import com.sparta.myblog.repository.PostRepository;
import com.sparta.myblog.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    // 댓글 작성하기
    public CommentResponseDto createComment
            (Long postId, CommentRequestDto requestDto, User user) {
        // 해당 게시글이 있는지
        Post post = this.findPost(postId);

        // DB에 댓글 저장하기.
        Comment comment = new Comment(requestDto);
        comment.setPost(post);
        comment.setUser(user);


        commentRepository.save(comment);

//        post.addComment(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        // 1. 해당하는 댓글 가져오기
        Comment comment = this.findComment(commentId);

        // 2. 댓글 작성자 이름 가져오기
        String commentUsername = comment.getUser().getUsername();

        // 3. 로그인한 유저가 해당 댓글 작성자인지 확인하기
        if(!commentUsername.equals(user.getUsername())) { // 작성자가 아닐 경우
            throw new IllegalArgumentException("댓글 작성자가 아닙니다.");
        }else { // 작성자가 맞을 경우
            // 4. 해당 댓글 수정하기
            comment.update(requestDto);
        }
        return new CommentResponseDto(comment);
    }

    public void deleteComment(HttpServletResponse res, Long commentId, User user) throws IOException {
        // 1. 해당하는 댓글 가져오기
        Comment comment = this.findComment(commentId);

        // 2. 댓글 작성자 이름 가져오기
        String commentUsername = comment.getUser().getUsername();

        // 3. 로그인한 유저가 해당 댓글 작성자인지 확인하기
        if(!commentUsername.equals(user.getUsername())) { // 작성자가 아닐 경우
            throw new IllegalArgumentException("댓글 작성자가 아닙니다.");
        }else { // 작성자가 맞을 경우
            // 4. 해당 댓글 수정하기
            commentRepository.delete(comment);
            responseResult(res,200,"댓글 삭제 성공");
        }
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
    }

    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
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
