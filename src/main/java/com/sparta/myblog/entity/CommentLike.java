package com.sparta.myblog.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="commentLikes")
@NoArgsConstructor
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean liked;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "commentId", nullable = false)
    private Comment comment;

    public void CommentLike(User user, Comment comment){
        this.user = user;
        this.comment = comment;
        this.liked = true;
    }

    public void changeLiked() {
        if(this.liked){
            this.liked = false;
        }else {
            this.liked = true;
        }
    }
}
