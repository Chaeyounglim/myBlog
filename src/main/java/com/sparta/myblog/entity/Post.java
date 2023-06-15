package com.sparta.myblog.entity;

import com.sparta.myblog.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "post")
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contents", nullable = false, length = 500)
    private String contents;

    @Column(name = "password", nullable = false)
    private String password;

    public Post(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.name = postRequestDto.getName();
        this.contents = postRequestDto.getContents();
        this.password = postRequestDto.getPassword();
    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.name = postRequestDto.getName();
        this.contents = postRequestDto.getContents();
        this.password = postRequestDto.getPassword();
    }

}