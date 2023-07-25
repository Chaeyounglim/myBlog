package com.sparta.myblog.service;

import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.entity.Post;
import com.sparta.myblog.entity.PostLike;
import com.sparta.myblog.entity.User;
import com.sparta.myblog.exception.PostNotFoundException;
import com.sparta.myblog.repository.PostLikeRepository;
import com.sparta.myblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository likeRepository;
    private final PostRepository postRepository;

    @Transactional
    public ResponseEntity<RestApiResponseDto> increaseLike(Long postId, User user) {
        // 1. 좋아요를 누른 게시글의 데이터가 있는지 확인
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException("해당 게시글이 존재하지 않습니다."));

        // 3. 현재 로그인한 사용자가 해당 게시글을 좋아요를 눌렀는지 판단 -> 2번 누를수 없음.
        Optional<PostLike> checkLike = likeRepository.findByUserIdAndPostId(user.getId(),postId);

        // 3-1. likes 테이블에 data 가 있을 경우
        if(checkLike.isPresent()){
            if(checkLike.get().isLiked()) {
                // liked 필드가 true 일 경우
                throw new IllegalArgumentException("좋아요 증가가 중복되었습니다.");
            }else {
                // liked 필드가 false 일 경우
                PostLike like = checkLike.get();
                like.changeLiked();
            }
        } // 3-2. likes 테이블에 없을 경우
        else {
            PostLike like = new PostLike(user,post); // user_id , post_id , liked 데이터를 추가
            likeRepository.save(like); // DB table 에 저장.
        }

        return getRestApiResponseDtoResponseEntity(
                "좋아요 성공", HttpStatus.OK,null);
    }


    @Transactional
    public ResponseEntity<RestApiResponseDto> decreaseLike(Long postId, User user) {
        // 1. 좋아요를 누른 게시글의 데이터가 있는지 확인
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException("해당 게시글이 존재하지 않습니다."));

        // 2. 현재 로그인한 사용자가 해당 게시글을 좋아요를 눌렀는지 판단 -> 눌렀을 경우 liked 필드값만 변경.
        Optional<PostLike> checkLike = likeRepository.findByUserIdAndPostId(user.getId(),postId);

        // 3. 좋아요 취소를 위해 likes 테이블에서 데이터 제거
        if(checkLike.isPresent()){ // like 테이블에 있을 경우
            if(checkLike.get().isLiked()){ // liked 칼럼이 true 일 경우
                PostLike like = checkLike.get();
                like.changeLiked(); // true 값을 false 로 변경
            }else{ // liked 칼럼이 false 일 경우
                throw new IllegalArgumentException("좋아요 감소가 중복되었습니다.");
/*                return getRestApiResponseDtoResponseEntity(
                        "좋아요 취소 실패: 이미 취소가 되어 있습니다.", HttpStatus.BAD_REQUEST);*/
            }
        } else { // 좋아요가 없을 경우
            throw new IllegalArgumentException("해당 좋아요에 대한 데이터가 없습니다.");
/*            return getRestApiResponseDtoResponseEntity(
                    "좋아요 취소 실패: 좋아요를 누른 적이 없습니다.", HttpStatus.NOT_FOUND);*/
        }
        return getRestApiResponseDtoResponseEntity(
                "좋아요 취소 성공",HttpStatus.OK,null);
    }



    private ResponseEntity<RestApiResponseDto> getRestApiResponseDtoResponseEntity(
            String message,HttpStatus status, Object result) {
        RestApiResponseDto restApiResponseDto = new RestApiResponseDto(status.value(),message,result);
        return new ResponseEntity<>(
                restApiResponseDto,
                status
        );
    }


}
