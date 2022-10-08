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
public class ProfileUpdateResponseDto {
    private Long profileId;
    private int age;
    private String mbti;
    private String introduction;
    private String idealType;
    private String job;
    private String hobby;
    private String drink;
    private String pet;
    private String smoke;
    private String likeMovieType;
    private String area;
}