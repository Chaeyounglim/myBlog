package com.sparta.myblog.entity;

import com.sparta.myblog.dto.LikeRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name="likes")
@NoArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Like(LikeRequestDto requestDto, User user, Post post) {
        this.user = user;
        this.post = post;
    }


}
