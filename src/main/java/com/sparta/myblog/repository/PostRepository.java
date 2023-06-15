package com.sparta.myblog.repository;

import com.sparta.myblog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    Post findPostByIdIsAndPasswordEquals(Long id, String password);

    List<Post> findAllByOrderByCreateAtDesc();

}