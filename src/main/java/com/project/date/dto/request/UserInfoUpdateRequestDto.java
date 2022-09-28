package com.project.date.dto.request;

import lombok.Getter;

import javax.persistence.Column;

@Getter
public class UserInfoUpdateRequestDto {

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
