package com.project.date.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long postId;
    private String title;
    private String nickname;
    private String content;
    private List<CommentResponseDto> commentResponseDtoList;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
