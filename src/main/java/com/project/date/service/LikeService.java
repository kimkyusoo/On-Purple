package com.project.date.service;

import com.project.date.dto.response.*;
import com.project.date.jwt.TokenProvider;
import com.project.date.model.*;
import com.project.date.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LikeService {

    private final TokenProvider tokenProvider;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UnLikeRepository unLikeRepository;
    private final UserRepository userRepository;

    // 게시글 좋아요
    @Transactional
    public ResponseDto<?> PostLike(Long postId,
                                   HttpServletRequest request) {

        if (null == request.getHeader("RefreshToken")) {
            return ResponseDto.fail("USER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("USER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        User user = validateUser(request);
        if (null == user) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글입니다.");
        }

        if (!post.validateUser(user)) {
            return ResponseDto.fail("BAD_REQUEST", "본인에게 좋아요 할 수 없습니다.");
        }
        //좋아요 한 적 있는지 체크
        Likes liked = likeRepository.findByUserAndPostId(user, postId).orElse(null);

        if (liked == null) {
            Likes postLike = Likes.builder()
                    .user(user)
                    .post(post)
                    .build();
            likeRepository.save(postLike);
            post.addLike();
            return ResponseDto.success(
                    LikeResponseDto.builder()
                            .likes(postLike.getPost().getLikes())
                            .build()
            );
        } else {
            likeRepository.delete(liked);
            post.minusLike();
            return ResponseDto.success(false);

        }
    }

    // 댓글 좋아요
    @Transactional
    public ResponseDto<?> CommentLike(Long commentId,
                                      HttpServletRequest request) {

        if (null == request.getHeader("RefreshToken")) {
            return ResponseDto.fail("USER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("USER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        User user = validateUser(request);
        if (null == user) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Comment comment = isPresentComment(commentId);
        if (null == comment)
            return ResponseDto.fail("COMMENT_NOT_FOUND", "댓글을 찾을 수 없습니다.");

        if (!comment.validateUser(user)) {
            return ResponseDto.fail("BAD_REQUEST", "본인에게 좋아요 할 수 없습니다.");
        }

        //좋아요 한 적 있는지 체크
        Likes liked = likeRepository.findByUserAndCommentId(user, commentId).orElse(null);

        if (liked == null) {
            Likes commentLike = Likes.builder()
                    .user(user)
                    .comment(comment)
                    .build();
            likeRepository.save(commentLike);
            comment.addLike();
            return ResponseDto.success(
                    LikeResponseDto.builder()
                            .likes(commentLike.getComment().getLikes()).build()
            );
        } else {
            likeRepository.delete(liked);
            comment.minusLike();
            return ResponseDto.success(false);
        }
    }

    //회원 좋아요
    @Transactional
    public ResponseDto<?> UserLike(Long targetId,
                                      HttpServletRequest request) {

        if (null == request.getHeader("RefreshToken")) {
            return ResponseDto.fail("USER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("USER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        User user = validateUser(request);
        if (null == user) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        User target = isPresentTarget(targetId);
        if (null == target)
            return ResponseDto.fail("PROFILE_NOT_FOUND", "타겟 유저를 찾을 수 없습니다.");


        //좋아요 한 적 있는지 체크
        Likes liked = likeRepository.findByUserAndTargetId(user, targetId).orElse(null);

        if (liked == null) {
            Likes userLike = Likes.builder()
                    .user(user)
                    .target(target)
                    .build();
            likeRepository.save(userLike);
            user.addLike();
            return ResponseDto.success("좋아요 성공");
        } else {
            likeRepository.delete(liked);
            user.minusLike();
            return ResponseDto.success("좋아요가 취소되었습니다.");
        }
    }

    //회원 싫어요
    public ResponseDto<?> ProfileUnLike(Long targetId,
                                        HttpServletRequest request) {

        if (null == request.getHeader("RefreshToken")) {
            return ResponseDto.fail("USER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("USER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        User user = validateUser(request);
        if (null == user) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        User target = isPresentTarget(targetId);
        if (null == target)
            return ResponseDto.fail("PROFILE_NOT_FOUND", "타겟 유저를 찾을 수 없습니다.");


        //좋아요 한 적 있는지 체크
        UnLike unLiked = unLikeRepository.findByUserAndTargetId(user, targetId).orElse(null);

        if (unLiked == null) {
            UnLike userUnLike = UnLike.builder()
                    .user(user)
                    .target(target)
                    .build();
            unLikeRepository.save(userUnLike);
            user.addUnLike();
            return ResponseDto.success("싫어요 성공");
        } else {
            unLikeRepository.delete(unLiked);
            user.minusUnLike();
            return ResponseDto.success("싫어요가 취소되었습니다.");
        }
    }



    @Transactional(readOnly = true)
    public User isPresentTarget(Long targetId) {
        Optional<User> optionalTarget = userRepository.findById(targetId);
        return optionalTarget.orElse(null);
    }




    @Transactional(readOnly = true)
    public Post isPresentPost(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        return optionalPost.orElse(null);
    }

    @Transactional(readOnly = true)
    public Comment isPresentComment(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        return optionalComment.orElse(null);
    }


    public User validateUser(HttpServletRequest request){
        if(!tokenProvider.validateToken(request.getHeader("RefreshToken"))){
            return null;
        }return tokenProvider.getUserFromAuthentication();
    }


}
