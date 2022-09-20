package com.project.date.controller;


import com.project.date.dto.request.PostRequestDto;
import com.project.date.dto.response.ResponseDto;
import com.project.date.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@RestController
public class PostController {

  private final PostService postService;


  // 게시글 작성
  @PostMapping( "/post")
  public ResponseDto<?> createPost(@RequestPart(value = "data") PostRequestDto requestDto,
                                   HttpServletRequest request) {
    return postService.createPost(requestDto,request);
  }

  // 상세 게시글 가져오기
  @GetMapping( "/post/{postId}")
  public ResponseDto<?> getPost(@PathVariable Long postId) {
    return postService.getPost(postId);
  }

  // 전체 게시글 가져오기
  @GetMapping("/post")
  public ResponseDto<?> getAllPosts() {
    return postService.getAllPost();
  }


  // 게시글 수정
  @PutMapping( "/post/{postId}")
  public ResponseDto<?> updatePost(@PathVariable Long postId,
                                   @RequestPart(value = "data") PostRequestDto requestDto,
                                   HttpServletRequest request) {
    return postService.updatePost(postId, requestDto, request);
  }

  //게시글 삭제
  @DeleteMapping( "/post/{postId}")
  public ResponseDto<?> deletePost(@PathVariable Long postId,
      HttpServletRequest request) {
    return postService.deletePost(postId, request);
  }


}
