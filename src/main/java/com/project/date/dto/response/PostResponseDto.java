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
    private String imageUrl;
    private Integer likes;
    private Integer view;
    private String category;
    private List<String> imgList;
    private List<CommentResponseDto> commentResponseDtoList;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public PostResponseDto(Long postId, String title, String nickname, String content,
                           String imageUrl, Integer likes, Integer view, String category,
                           List<String> imgList, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.postId = postId;
        this.title = title;
        this.nickname = nickname;
        this.content = content;
        this.imageUrl = imageUrl;
        this.likes = likes;
        this.view = view;
        this.category = category;
        this.imgList = imgList;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}


