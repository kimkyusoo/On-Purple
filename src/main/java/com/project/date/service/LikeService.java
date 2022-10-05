package com.project.date.service;

import com.project.date.dto.response.LikeResponseDto;
import com.project.date.dto.response.ResponseDto;
import com.project.date.jwt.TokenProvider;
import com.project.date.model.*;
import com.project.date.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
public class LikeService {

    private final TokenProvider tokenProvider;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ProfileRepository profileRepository;
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
            return ResponseDto.success("좋아요 성공");
        } else {
            likeRepository.delete(liked);
            post.minusLike();
            return ResponseDto.success("좋아요가 취소되었습니다.");
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
        Likes liked = likeRepository.findByUserAndCommentId(user,commentId).orElse(null);

        if (liked == null) {
            Likes commentLike = Likes.builder()
                    .user(user)
                    .comment(comment)
                    .build();
            likeRepository.save(commentLike);
            comment.addLike();
            return ResponseDto.success("좋아요 성공");
        } else {
            likeRepository.delete(liked);
            comment.minusLike();
            return ResponseDto.success("좋아요가 취소되었습니다.");
        }
    }

    //회원 좋아요
    @Transactional
    public ResponseDto<?> ProfileLike(Long profileId,
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

        Profile profile = isPresentProfile(profileId);
        if (null == profile)
            return ResponseDto.fail("PROFILE_NOT_FOUND", "프로필을 찾을 수 없습니다.");

        if (!profile.validateUser(user)) {
            return ResponseDto.fail("BAD_REQUEST", "본인에게 좋아요 할 수 없습니다.");
        }

        //좋아요 한 적 있는지 체크
        Likes liked = likeRepository.findByUserAndProfileId(user,profileId).orElse(null);

        if (liked == null) {
            Likes profileLike = Likes.builder()
                    .user(user)
                    .profile(profile)
                    .build();
            likeRepository.save(profileLike);
            profile.addLike();
            return ResponseDto.success("좋아요 성공");
        } else {
            likeRepository.delete(liked);
            profile.minusLike();
            return ResponseDto.success("좋아요가 취소되었습니다.");
        }
    }
    //회원 싫어요
    public ResponseDto<?> ProfileUnLike(Long profileId,
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

        Profile profile = isPresentProfile(profileId);
        if (null == profile)
            return ResponseDto.fail("PROFILE_NOT_FOUND", "프로필을 찾을 수 없습니다.");

        if (!profile.validateUser(user)) {
            return ResponseDto.fail("BAD_REQUEST", "본인에게 싫어요 할 수 없습니다.");
        }


        //좋아요 한 적 있는지 체크
        UnLike unLiked = unLikeRepository.findByUserAndProfileId(user,profileId).orElse(null);

        if (unLiked == null) {
            UnLike profileUnLike = UnLike.builder()
                    .user(user)
                    .profile(profile)
                    .build();
            unLikeRepository.save(profileUnLike);
            profile.addUnLike();
            return ResponseDto.success("싫어요 성공");
        } else {
            unLikeRepository.delete(unLiked);
            profile.minusUnLike();
            return ResponseDto.success("싫어요가 취소되었습니다.");
        }
    }
    @Transactional(readOnly = true)
    public User isPresentUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.orElse(null);
    }



    @Transactional(readOnly = true)
    public Profile isPresentProfile(Long profileId) {
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);
        return optionalProfile.orElse(null);
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
