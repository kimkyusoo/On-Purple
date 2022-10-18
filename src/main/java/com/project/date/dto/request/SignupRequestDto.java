package com.project.date.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    @NotBlank
    @Size(min = 4, max = 12)
//    숫자 0~9, 영문자 대소문자 포함해서 최소 4자 최대 12자를 의미 a-zA-Z: 영문자, 대소문자, []: 문자의 집합이나 범위를 나타내며 문자사이는 -기호로 범위를 나타냄,
//    \d: 숫자 0~9,  * : 앞의 문자가 없을수도 있을수도 있다는 것을 뜻함, $: 문자열 종료 , {} : 횟수 or 범위
    @Pattern(regexp = "[a-zA-Z\\d]*${4,12}")
    private String username;

    @NotBlank
    @Size(min = 2, max = 8)
    @Pattern(regexp = "^([가-힣]+[0-9]*)${2,8}")
    private String nickname;

    @NotBlank
    @Size(min = 4, max = 20)
    @Pattern(regexp = "[a-zA-Z\\d]*${4,20}")
    private String password;

    @NotBlank
    private String passwordConfirm;

    private String gender;

    private boolean admin = false;

    private String adminToken = "";

    private String imageUrl;

    private Integer age;

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
