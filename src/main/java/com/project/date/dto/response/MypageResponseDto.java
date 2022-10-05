package com.project.date.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MypageResponseDto{
    private Long profileId;
    private String nickname;
    private String imageUrl;
    private Long age;
    private String mbti;
    private String introduction;
    private String area;
    private List<LikeResponseDto> likeResponseDtoList;
    private List<LikedResponseDto> likedResponseDtoList;
}