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

    // 댓글 좋아요
    @PostMapping( "/user/like/{profileId}")
    public ResponseDto<?> createProfileLike(@PathVariable Long profileId, HttpServletRequest request) {
        return likeService.ProfileLike(profileId, request);
    }

    // 댓글 좋아요
    @PostMapping( "/user/unlike/{profileId}")
    public ResponseDto<?> createProfileUnLike(@PathVariable Long profileId, HttpServletRequest request) {
        return likeService.ProfileUnLike(profileId, request);
    }
/*
    @PostMapping("/user/check/{profileId}")
    public ResponseDto<?> likeCheck(@PathVariable Long profileId, HttpServletRequest request ){
        return likeService.likeCheck(request,profileId);
    }

    @PostMapping("/user/checks/{profileId}")
    public ResponseDto<?> likeChecks(@PathVariable Long profileId, HttpServletRequest request ){
        return likeService.likeCheck2(request,profileId);
    }*/

    @PostMapping("/user/checks/{userId}")
    public ResponseDto<?> likeChecks(@PathVariable Long userId, HttpServletRequest request ){
        return likeService.getAllLike(userId,request);
    }

}
