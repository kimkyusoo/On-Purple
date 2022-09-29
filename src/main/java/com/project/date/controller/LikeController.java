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

}
