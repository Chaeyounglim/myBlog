package com.sparta.myblog.repository;

import com.sparta.myblog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllByPostIdOrderByCreatedAtDesc(long id);

    @Query(value = "SELECT * FROM member WHERE :field = :value", nativeQuery=true)
    List<Comment> findByCondition(@Param("field") String field, @Param("value")String value);

}
