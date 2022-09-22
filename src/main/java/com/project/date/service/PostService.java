package com.project.date.service;


import com.project.date.dto.request.PostRequestDto;
import com.project.date.dto.response.CommentResponseDto;
import com.project.date.dto.response.PostResponseDto;
import com.project.date.dto.response.ResponseDto;
import com.project.date.jwt.TokenProvider;
import com.project.date.model.Comment;
import com.project.date.model.Post;
import com.project.date.model.User;
import com.project.date.repository.CommentRepository;
import com.project.date.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final TokenProvider tokenProvider;
  private final CommentRepository commentRepository;

  // 게시글 작성
  @Transactional
  public ResponseDto<?> createPost(PostRequestDto requestDto,
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

    Post post = Post.builder()
            .user(user)
            .title(requestDto.getTitle())
            .content(requestDto.getContent())
            .build();

    postRepository.save(post);
    return ResponseDto.success(
        PostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .nickname(post.getUser().getNickname())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build()
    );
  }

  // 게시글 단건 조회
  @Transactional// readOnly설정시 데이터가 Mapping되지 않는문제로 해제
  public ResponseDto<?> getPost(Long postId) {
    Post post = isPresentPost(postId);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글입니다.");
    }
      List<Comment> commentList = commentRepository.findAllByPost(post);
      List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

      for (Comment comment : commentList) {
          commentResponseDtoList.add(
                  CommentResponseDto.builder()
                          .commentId(comment.getId())
                          .nickname(comment.getUser().getNickname())
                          .comment(comment.getComment())
                          .createdAt(comment.getCreatedAt())
                          .modifiedAt(comment.getModifiedAt())
                          .build()
          );
      }

      return ResponseDto.success(
              PostResponseDto.builder()
                      .postId(post.getId())
                      .title(post.getTitle())
                      .content(post.getContent())
                      .commentResponseDtoList(commentResponseDtoList)
                      .nickname(post.getUser().getNickname())
                      .createdAt(post.getCreatedAt())
                      .modifiedAt(post.getModifiedAt())
                      .build()
      );
  }

  // 전체 게시글 조회
  @Transactional(readOnly = true)
  public ResponseDto<?> getAllPost() {
    List<Post> postList = postRepository.findAllByOrderByModifiedAtDesc();
    List<PostResponseDto> postResponseDto = new ArrayList<>();
    for (Post post : postList) {
      postResponseDto.add(
              PostResponseDto.builder()
                      .postId(post.getId())
                      .title(post.getTitle())
                      .nickname(post.getUser().getNickname())
                      .createdAt(post.getCreatedAt())
                      .modifiedAt(post.getModifiedAt())
                      .build()
      );
    }

    return ResponseDto.success(postResponseDto);

  }



    @Transactional
    public ResponseDto<PostResponseDto> updatePost(Long postId, PostRequestDto requestDto, HttpServletRequest request) {
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

        if (post.validateUser(user)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        post.update(requestDto);
        return ResponseDto.success(
                PostResponseDto.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .nickname(post.getUser().getNickname())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> deletePost(Long postId, HttpServletRequest request) {
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

        if (post.validateUser(user)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
        return ResponseDto.success("delete success");
    }

  @Transactional(readOnly = true)
  public Post isPresentPost(Long postId) {
    Optional<Post> optionalPost = postRepository.findById(postId);
    return optionalPost.orElse(null);
  }

  @Transactional
  public User validateUser(HttpServletRequest request) {
    if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
      return null;
    }
    return tokenProvider.getUserFromAuthentication();
  }
}
