package com.project.date.controller;

import javax.servlet.http.HttpServletRequest;

import com.project.date.dto.request.CommentRequestDto;
import com.project.date.dto.response.ResponseDto;
import com.project.date.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/comment")
  public ResponseDto<?> createComment(@RequestBody CommentRequestDto requestDto,
                                      HttpServletRequest request) {
    return commentService.createComment(requestDto, request);
  }

  @GetMapping("/comment")
  public ResponseDto<?> getAllComments(@PathVariable Long id) {
    return commentService.getAllCommentsByPost(id);
  }

  @PutMapping( "/comment/{id}")
  public ResponseDto<?> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto,
      HttpServletRequest request) {
    return commentService.updateComment(id, requestDto, request);
  }

  @DeleteMapping( "/comment/{id}")
  public ResponseDto<?> deleteComment(@PathVariable Long id,
      HttpServletRequest request) {
    return commentService.deleteComment(id, request);
  }
}
