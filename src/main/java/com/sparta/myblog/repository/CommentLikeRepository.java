package com.sparta.myblog.repository;

import com.sparta.myblog.entity.CommentLike;
import com.sparta.myblog.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    List<CommentLike> findByCommentId(Long commentId);

    Optional<CommentLike> findByUserIdAndCommentId(Long user_id, Long comment_id);
}
