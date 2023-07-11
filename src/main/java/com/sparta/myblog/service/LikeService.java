package com.sparta.myblog.service;

import com.sparta.myblog.dto.LikeRequestDto;
import com.sparta.myblog.dto.PostResponseDto;
import com.sparta.myblog.dto.RestApiResponseDto;
import com.sparta.myblog.entity.Like;
import com.sparta.myblog.entity.Post;
import com.sparta.myblog.entity.User;
import com.sparta.myblog.repository.LikeRepository;
import com.sparta.myblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;


    @Transactional
    public ResponseEntity<RestApiResponseDto> like(Long post_id, LikeRequestDto requestDto, User user) {
        Optional<Post> checkPost = postRepository.findById(post_id);
        if(!checkPost.isPresent()){ // 좋아요 게시글이 없을 경우
            RestApiResponseDto apiResponseDto = new RestApiResponseDto("해당 게시글이 없습니다.", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(
                    apiResponseDto,
                    HttpStatus.NOT_FOUND
            );
        }

        Post post = checkPost.get();
        Long likeCnt = post.getLikeCount();

        // 현재 로그인한  사용자가 해당 게시글을 좋아요를 눌렀는지 판단
        Optional<Like> CheckLike = likeRepository.findByUserIdAndPostId(user.getId(),post_id);

        if(CheckLike.isPresent()){ // 좋아요 취소를 위해 likes 테이블에서 데이터 제거
            likeRepository.delete(CheckLike.get());
            post.decreaseLike();
        }else {
            Like like = new Like(requestDto,user,post);
            likeRepository.save(like);
            post.increaseLike();
        }

        // - 사용자가 이미 ‘좋아요’한 게시글에 다시 ‘좋아요’ 요청을 하면 ‘좋아요’를 했던 기록이 취소됩니다.
        //- 요청이 성공하면 Client 로 성공했다는 메시지, 상태코드 반환하기

        RestApiResponseDto restApiException = new RestApiResponseDto("좋아요 성공", HttpStatus.OK.value());
        return new ResponseEntity<>(
                restApiException,
                HttpStatus.OK
        );
    }
}
