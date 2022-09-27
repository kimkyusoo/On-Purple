package com.project.date.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto {
    private Long profileId;
//    private List<UserResponseDto> userResponseDtoList;
    private String imageUrl;
    private String nickname;
    private int age;
    private String mbti;
    private String introduction;
    private String idealType;
    private String job;
    private String hobby;
    private String pet;
    private String smoke;
    private String likeMovieType;
    private String area;

}
