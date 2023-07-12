package com.sparta.myblog.service;

import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.entity.Post;
import com.sparta.myblog.entity.PostLike;
import com.sparta.myblog.entity.User;
import com.sparta.myblog.exception.PostNotFoundException;
import com.sparta.myblog.repository.PostLikeRepository;
import com.sparta.myblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository likeRepository;
    private final PostRepository postRepository;
    private final MessageSource messageSource;

    @Transactional
    public ResponseEntity<RestApiResponseDto> increaseLike(Long postId, User user) {
        // 1. 좋아요를 누른 게시글의 데이터가 있는지 확인
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException(messageSource.getMessage(
                        "not.found.post",
                        null,
                        "Not Found post",
                        Locale.getDefault()
                ))
        );

/*        Optional<Post> checkPost = postRepository.findById(postId);

        if(!checkPost.isPresent()){
            return getRestApiResponseDtoResponseEntity(
                    "해당 게시글이 없습니다.", HttpStatus.NOT_FOUND);
        }

        // 2. 해당 게시글에 좋아요 cnt 를 증가시키기 위해 가져옴.
        Post post = checkPost.get();*/

        // 3. 현재 로그인한 사용자가 해당 게시글을 좋아요를 눌렀는지 판단 -> 2번 누를수 없음.
        Optional<PostLike> checkLike = likeRepository.findByUserIdAndPostId(user.getId(),postId);

        // 3-1. likes 테이블에 data 가 있을 경우
        if(checkLike.isPresent()){
            if(checkLike.get().isLiked()) {
                // liked 필드가 true 일 경우
                throw new IllegalArgumentException(
                        messageSource.getMessage(
                                "duplicated.increase.like",
                                null,
                                "duplicated increase like",
                                Locale.getDefault() // 국제화하는 것임.
                        )
                );
/*                return getRestApiResponseDtoResponseEntity(
                        "좋아요 실패: 좋아요가 이미 눌러져 있습니다.", HttpStatus.BAD_REQUEST);*/
            }else {
                // liked 필드가 false 일 경우
                PostLike like = checkLike.get();
                like.changeLiked();
                post.increaseLike();
            }
        } // 3-2. likes 테이블에 없을 경우
        else {
            PostLike like = new PostLike(user,post); // user_id , post_id , liked 데이터를 추가
            likeRepository.save(like); // DB table 에 저장.
            post.increaseLike(); // post table 에서도 likeCnt 증가 (Transaction 환경이여야 함)
        }

        return getRestApiResponseDtoResponseEntity(
                "좋아요 성공", HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<RestApiResponseDto> decreaseLike(Long postId, User user) {
        // 1. 좋아요를 누른 게시글의 데이터가 있는지 확인
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException(messageSource.getMessage(
                        "not.found.post",
                        null,
                        "Not Found post",
                        Locale.getDefault()
                ))
        );

        // 2. 현재 로그인한 사용자가 해당 게시글을 좋아요를 눌렀는지 판단 -> 눌렀을 경우 liked 필드값만 변경.
        Optional<PostLike> checkLike = likeRepository.findByUserIdAndPostId(user.getId(),postId);

        // 3. 좋아요 취소를 위해 likes 테이블에서 데이터 제거
        if(checkLike.isPresent()){ // like 테이블에 있을 경우
            if(checkLike.get().isLiked()){ // liked 칼럼이 true 일 경우
                PostLike like = checkLike.get();
                like.changeLiked(); // true 값을 false 로 변경
                post.decreaseLike(); // post table 에서도 likeCnt 감소 (Transaction 환경이여야 함)
            }else{ // liked 칼럼이 false 일 경우
                throw new IllegalArgumentException(
                        messageSource.getMessage(
                                "duplicated.decrease.like",
                                null,
                                "duplicated decrease like",
                                Locale.getDefault() // 국제화하는 것임.
                        )
                );
/*                return getRestApiResponseDtoResponseEntity(
                        "좋아요 취소 실패: 이미 취소가 되어 있습니다.", HttpStatus.BAD_REQUEST);*/
            }
        } else { // 좋아요가 없을 경우
            throw new IllegalArgumentException(
                    messageSource.getMessage(
                            "not.found.like",
                            null,
                            "not found like",
                            Locale.getDefault() // 국제화하는 것임.
                    )
            );
/*            return getRestApiResponseDtoResponseEntity(
                    "좋아요 취소 실패: 좋아요를 누른 적이 없습니다.", HttpStatus.NOT_FOUND);*/
        }
        return getRestApiResponseDtoResponseEntity(
                "좋아요 취소 성공",HttpStatus.OK);
    }



    private ResponseEntity<RestApiResponseDto> getRestApiResponseDtoResponseEntity(
            String message,HttpStatus status) {
        RestApiResponseDto restApiException = new RestApiResponseDto(message, status.value());
        return new ResponseEntity<>(
                restApiException,
                status
        );
    }


}
