package com.sparta.myblog.repository;

import com.sparta.myblog.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {

    Optional<PostLike> findByUserIdAndPostId(Long user_id, Long post_id);
}
