package com.sparta.myblog.service;

import com.sparta.myblog.dto.PostRequestDto;
import com.sparta.myblog.dto.PostResponseDto;
import com.sparta.myblog.entity.Post;
import com.sparta.myblog.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public List<PostResponseDto> getPostList() {
        return postRepository.findAllByOrderByCreateAtDesc().stream().map(PostResponseDto::new).toList();
    }

    public PostResponseDto getPost(Long id) {
        Post post = findPost(id);
        return new PostResponseDto(post);
    }


    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        Post post = new Post(postRequestDto);
        Post savePost = postRepository.save(post);
        return new PostResponseDto(post);
    }


    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto) {
        Post post = findPost(id);
        post = checkPassword(id, postRequestDto.getPassword());

        if (!(post == null)) { // 해당 객체가 있을 경우
            post.update(postRequestDto);
        } else { // 해당 객체가 없을 경우
            throw new IllegalArgumentException("비밀번호가 일치 하지 않습니다.");
        }
        return new PostResponseDto(post);
    }

    public boolean deletePost(Long id, String password) {
        Post post = findPost(id);
        post = checkPassword(id, password);

        if (!(post == null)) { // 해당 객체가 있을 경우
            postRepository.delete(post);
        } else { // 해당 객체가 없을 경우
            throw new IllegalArgumentException("비밀번호가 일치 하지 않습니다.");
        }
        return !postRepository.existsById(id);
    }

    private Post checkPassword(Long id, String inputPassword) {
        return postRepository.findPostByIdIsAndPasswordEquals(id, inputPassword);
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
    }

}
