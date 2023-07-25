package com.sparta.myblog.service;

import com.sparta.myblog.dto.CommentRequestDto;
import com.sparta.myblog.dto.CommentResponseDto;
import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.entity.Comment;
import com.sparta.myblog.entity.Post;
import com.sparta.myblog.entity.User;
import com.sparta.myblog.exception.CommentNotFoundException;
import com.sparta.myblog.exception.PostNotFoundException;
import com.sparta.myblog.repository.CommentLikeRepository;
import com.sparta.myblog.repository.CommentRepository;
import com.sparta.myblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    // 댓글 작성하기
    @Override
    public ResponseEntity<RestApiResponseDto> createComment
    (CommentRequestDto requestDto, User user) {
        // 해당 게시글이 있는지
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() ->
                new PostNotFoundException("해당 게시글이 존재하지 않습니다."));

        // DB에 댓글 저장하기.
        Comment comment = new Comment(requestDto,post,user);

        commentRepository.save(comment);

        log.info("댓글 저장 완료");
        return getRestApiResponseDtoResponseEntity("댓글 작성 성공",HttpStatus.OK, new CommentResponseDto(comment));

    }


    // 댓글 수정하기
    @Override
    @Transactional
    public ResponseEntity<RestApiResponseDto> updateComment(
            Comment comment, CommentRequestDto requestDto, User user){
/*        // 1. 해당하는 댓글 가져오기
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException("해당 댓글이 존재하지 않습니다."));

        // 2. 댓글 작성자 이름 가져오기
        Long commentUserId = comment.getUser().getId();

        // 3. 로그인한 유저가 해당 댓글 작성자인지 확인하기
        if (!commentUserId.equals(user.getId()) && !user.getRole().equals(UserRoleEnum.ADMIN)) { // 작성자가 아닐 경우
            log.error("댓글 작성자가 아닌 사용자가 댓글 수정 요청");
            throw new IllegalArgumentException("작성자 혹은 관리자만 삭제/수정 할 수 있습니다.");
        } // the end of if()*/
        // 4. 해당 댓글 수정하기
        log.info("service" + comment.getId());
        log.info("service" + user.getId());
        comment.update(requestDto);
        log.info("service에서 저장 후" + comment.getId());
        return getRestApiResponseDtoResponseEntity("댓글 수정 성공",HttpStatus.OK, new CommentResponseDto(comment));
    }


    // 댓글 삭제하기
    @Override
    public ResponseEntity<RestApiResponseDto> deleteComment(Comment comment, User user) {
/*        // 1. 해당하는 댓글 가져오기
        Comment comment = findById();commentRepository.findById(commentId).orElseThrow(() ->
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
        commentLikeRepository.deleteAll(likeList);*/
        // 5. 해당 댓글 삭제하기
        log.info("service");
        commentRepository.delete(comment);
        return getRestApiResponseDtoResponseEntity("댓글 삭제 성공",HttpStatus.OK,null);
    }

    @Override
    public Comment findById(Long id){
        log.info(String.valueOf(id));
        return commentRepository.findById(id).orElseThrow(() ->
                new CommentNotFoundException("해당 댓글이 존재하지 않습니다."));
    }

    @Override
    public ResponseEntity<RestApiResponseDto> getRestApiResponseDtoResponseEntity(
            String message, HttpStatus status,Object result) {
        RestApiResponseDto restApiResponseDto = new RestApiResponseDto(status.value(), message,result);
        return new ResponseEntity<>(
                restApiResponseDto,
                status
        );
    }

}