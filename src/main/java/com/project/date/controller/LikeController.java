package com.project.date.controller;

import com.project.date.dto.response.ResponseDto;
import com.project.date.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
@RequiredArgsConstructor
@RestController
public class LikeController {

    private final LikeService likeService;


    // 게시글 좋아요
    @PostMapping( "/post/like/{postId}")
    public ResponseDto<?> createPostLike(@PathVariable Long postId, HttpServletRequest request) {
        return likeService.PostLike(postId, request);
    }
    // 댓글 좋아요
    @PostMapping( "/comment/like/{commentId}")
    public ResponseDto<?> createCommentLike(@PathVariable Long commentId, HttpServletRequest request) {
        return likeService.CommentLike(commentId, request);
    }

    // 회원 좋아요
    @PostMapping( "/user/like/{targetUserId}")
    public ResponseDto<?> createUserLike(@PathVariable Long targetUserId, HttpServletRequest request) {
        return likeService.UserLike(targetUserId, request);
    }

    // 회원 싫어요
    @PostMapping( "/user/unlike/{targetUserId}")
    public ResponseDto<?> createUserUnLike(@PathVariable Long targetUserId, HttpServletRequest request) {
        return likeService.ProfileUnLike(targetUserId, request);
    }

    @PostMapping("/user/checkJ/{userId}")
    public ResponseDto<?> likeCheckJ(@PathVariable Long userId, HttpServletRequest request ){
        return likeService.likeCheckJ(userId,request);
    }


    @PostMapping("/user/checks/{userId}")
    public ResponseDto<?> likeChecks(@PathVariable Long userId, HttpServletRequest request ){
        return likeService.getAllLike(userId,request);
    }

    @PostMapping("/user/matching/{userId}")
    public ResponseDto<?> likeChecksFinal(@PathVariable Long userId, HttpServletRequest request ){
        return likeService.likeCheckFinal(userId,request);
    }

    @PostMapping("/user/match/{userId}")
    public ResponseDto<?> likeCheckFinalReal(@PathVariable Long userId, HttpServletRequest request ){
        return likeService.likessCheck(request,userId);
    }
}
