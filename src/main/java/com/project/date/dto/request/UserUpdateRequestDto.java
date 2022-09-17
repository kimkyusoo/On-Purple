package com.project.date.dto.request;


import lombok.Getter;

@Getter
public class UserUpdateRequestDto {

    private String nickname;
    private String password;
    private String imageUrl;

}
