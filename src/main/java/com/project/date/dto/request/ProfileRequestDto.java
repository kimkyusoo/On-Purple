package com.project.date.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProfileRequestDto {

    private Long age;
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