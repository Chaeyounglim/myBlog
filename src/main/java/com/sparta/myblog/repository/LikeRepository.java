package com.sparta.myblog.repository;

import com.sparta.myblog.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {

    Optional<Like> findByUserIdAndPostId(Long user_id, Long post_id);
}
