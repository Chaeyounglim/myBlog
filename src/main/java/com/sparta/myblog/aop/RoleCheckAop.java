package com.sparta.myblog.aop;

import com.sparta.myblog.entity.Comment;
import com.sparta.myblog.entity.Post;
import com.sparta.myblog.entity.User;
import com.sparta.myblog.entity.UserRoleEnum;
import com.sparta.myblog.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.concurrent.RejectedExecutionException;

@Slf4j
@Aspect
@Component
public class RoleCheckAop {

    @Pointcut("execution(* com.sparta.myblog.service.PostService.updatePost(..))")
    private void updatePost() {}

    @Pointcut("execution(* com.sparta.myblog.service.PostService.deletePost(..))")
    private void deletePost() {}

    @Pointcut("execution(* com.sparta.myblog.service.CommentService.updateComment(..))")
    private void updateComment() {}

    @Pointcut("execution(* com.sparta.myblog.service.CommentService.deleteComment(..))")
    private void deleteComment() {}

    @Around( "updatePost() || deletePost()")
    public Object executePostRoleCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        // 첫번째 매개 변수로 게시글 받아옴
        Post post = (Post)joinPoint.getArgs()[0];

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.getPrincipal().getClass() == UserDetailsImpl.class){
            // 로그인 회원 정보 가져오기
            UserDetailsImpl userDetails = (UserDetailsImpl)auth.getPrincipal();
            User loginUser = userDetails.getUser();
            log.info("aop loginUser : " + loginUser);
            log.info("aop post.getUser(): " + post.getUser());


            // 게시글 작성자와 요청자가 같은지 또는 관리자 권한의 유저인지 확인
            if(! (loginUser.getRole().equals(UserRoleEnum.ADMIN) || post.getUser().equals(loginUser)) ){
                throw new RejectedExecutionException();
            }
        }

        // 핵심 기능 수행
        return joinPoint.proceed();
    }

    @Around("updateComment() || deleteComment()")
    public Object executeCommentRoleCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        // 첫번째 매개변수로 게시글 받아옴
        Comment comment = (Comment)joinPoint.getArgs()[0];

        // 로그인 회원이 없는 경우, 수행시간 기록하지 않음
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal().getClass() == UserDetailsImpl.class) {
            // 로그인 회원 정보
            UserDetailsImpl userDetails = (UserDetailsImpl)auth.getPrincipal();
            User loginUser = userDetails.getUser();
            log.info("aop: " + loginUser);
            log.info("aop: " + comment.getUser());

            // 댓글 작성자(comment.user) 와 요청자(user) 가 같은지 또는 Admin 인지 체크 (아니면 예외발생)
            if (!(loginUser.getRole().equals(UserRoleEnum.ADMIN) || comment.getUser().equals(loginUser))) {
                log.warn("[AOP] 작성자만 댓글을 수정/삭제 할 수 있습니다.");
                throw new RejectedExecutionException();
            }
        }

        // 핵심기능 수행
        return joinPoint.proceed();
    }

}
